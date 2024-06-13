package org.proyecto.fastdeliveryp_v1.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utilidad para manejar operaciones con JWT, como creación y validación de tokens.
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key key;

    /**
     * Inicializa la clave de firma HMAC usando la clave secreta.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Crea un token JWT para un usuario con un rol específico.
     *
     * @param email el email del usuario.
     * @param role  el rol del usuario.
     * @return el token JWT.
     */
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Crea un token JWT para el restablecimiento de contraseña.
     *
     * @param email el email del usuario.
     * @return el token JWT para el restablecimiento de contraseña.
     */
    public String createPasswordResetToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 horas
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Valida un token JWT.
     *
     * @param token       el token JWT.
     * @param userDetails los detalles del usuario.
     * @return true si el token es válido, false de lo contrario.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String email = claims.getSubject();
            boolean isExpired = claims.getExpiration().before(new Date());
            return email.equals(userDetails.getUsername()) && !isExpired;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el nombre de usuario del token JWT.
     *
     * @param token el token JWT.
     * @return el nombre de usuario.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Obtiene el nombre de usuario del token JWT de restablecimiento de contraseña.
     *
     * @param token el token JWT.
     * @return el nombre de usuario.
     */
    public String getUsernameFromPasswordResetToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene un valor específico de los claims del token JWT.
     *
     * @param token          el token JWT.
     * @param claimsResolver la función para resolver el valor de los claims.
     * @return el valor del claim.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene todos los claims del token JWT.
     *
     * @param token el token JWT.
     * @return los claims del token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Comprueba si el token JWT ha expirado.
     *
     * @param token el token JWT.
     * @return true si el token ha expirado, false de lo contrario.
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Obtiene la fecha de expiración del token JWT.
     *
     * @param token el token JWT.
     * @return la fecha de expiración.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}

