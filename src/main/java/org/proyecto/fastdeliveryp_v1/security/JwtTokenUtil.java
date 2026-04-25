package org.proyecto.fastdeliveryp_v1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utilidades para generar y validar tokens JWT (HS512).
 *
 * <p>El token contiene el email del usuario como {@code subject} y el rol como
 * claim custom. La clave de firma se deriva del secreto configurado en
 * {@code jwt.secret} usando {@link Keys#hmacShaKeyFor(byte[])}.</p>
 *
 * <p><b>Auditoría 2026-04 — cambios:</b></p>
 * <ul>
 *   <li>{@code secret.getBytes()} → {@code secret.getBytes(StandardCharsets.UTF_8)}.
 *       Antes usaba el encoding de la plataforma (default Windows/Linux distintos).
 *       Si el secreto contiene caracteres no ASCII, dos hosts con encodings
 *       distintos generaban tokens incompatibles.</li>
 *   <li>{@code e.printStackTrace()} → SLF4J. Los stack traces a System.err
 *       acaban en logs de Docker sin formato y sin nivel — ruido y leak.</li>
 *   <li>Validación de longitud mínima del secreto en {@link #init()} para que
 *       la app falle al arrancar si {@code JWT_SECRET} es débil.</li>
 * </ul>
 */
@Component
public class JwtTokenUtil {

    /** Longitud mínima del secreto JWT (64 chars ≈ 512 bits, igual al algoritmo HS512). */
    private static final int MIN_SECRET_LENGTH = 64;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    /** Tiempo de vida del token de sesión en segundos (configurable, ej: 3600 = 1h). */
    @Value("${jwt.expiration}")
    private Long expiration;

    /** Clave HMAC derivada del secreto. Inmutable después de {@link #init()}. */
    private Key key;

    /**
     * Inicializa la clave HMAC y valida que el secreto sea suficientemente largo.
     * Falla rápido si el secreto es débil.
     */
    @PostConstruct
    public void init() {
        if (secret == null || secret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException(
                "jwt.secret debe tener al menos " + MIN_SECRET_LENGTH + " caracteres. " +
                "Genera uno con: openssl rand -base64 64 | tr -d '\\n/+='"
            );
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token de sesión para un usuario con su rol.
     *
     * @param email email del usuario (subject del JWT).
     * @param role  rol asignado (ej. {@code ROLE_USER}, {@code ROLE_ADMIN}).
     * @return el token JWT compactado.
     */
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Genera un token específico para reset de contraseña (vida corta, 24h).
     *
     * @param email email del usuario que solicita el reset.
     * @return el token de reset.
     */
    public String createPasswordResetToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 24L * 60 * 60 * 1000))  // 24 horas
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Valida que el token sea de un usuario concreto y no haya expirado.
     *
     * <p>El orden importa: primero se intenta parsear (lo que verifica firma y
     * estructura). Sólo después se compara username y expiration. Si la firma
     * es inválida la excepción se captura y se devuelve {@code false}.</p>
     *
     * @param token       el JWT a validar.
     * @param userDetails los detalles del usuario contra quien se valida.
     * @return {@code true} si el token es válido para ese usuario.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String email = claims.getSubject();
            boolean expired = claims.getExpiration().before(new Date());
            return email.equals(userDetails.getUsername()) && !expired;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            log.warn("Token JWT inválido: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Extrae el subject (email) del token.
     *
     * @param token el JWT.
     * @return el email contenido en el subject.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extrae el subject del token de reset de contraseña.
     *
     * <p>Devuelve {@code null} si el token es inválido o expiró — el caller debe
     * tratar el null como "token no válido". Considera lanzar excepción en una
     * próxima iteración para forzar manejo explícito.</p>
     *
     * @param token el token de reset.
     * @return el email, o {@code null} si el token no se puede parsear.
     */
    public String getUsernameFromPasswordResetToken(String token) {
        try {
            return getAllClaimsFromToken(token).getSubject();
        } catch (Exception ex) {
            log.warn("Token de reset de password inválido: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Extrae un claim arbitrario del token aplicando una función.
     *
     * @param token          el JWT.
     * @param claimsResolver función que extrae el claim deseado de los Claims.
     * @param <T>            tipo del claim.
     * @return el valor extraído.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaimsFromToken(token));
    }

    /**
     * Parsea y verifica la firma del token, devolviendo todos sus claims.
     *
     * @param token el JWT.
     * @return el cuerpo de claims.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Comprueba si el token ha expirado (sin lanzar excepción si la firma es válida).
     *
     * @param token el JWT.
     * @return {@code true} si la fecha de expiración está en el pasado.
     */
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * Devuelve la fecha de expiración del token.
     *
     * @param token el JWT.
     * @return la {@link Date} de expiración.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
