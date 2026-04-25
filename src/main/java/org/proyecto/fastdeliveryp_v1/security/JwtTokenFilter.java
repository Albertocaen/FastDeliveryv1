package org.proyecto.fastdeliveryp_v1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.service.TokenRevocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que examina cada petición HTTP en busca de la cookie {@code JWT}.
 *
 * <p>Si encuentra un token válido (firma correcta, no expirado, no revocado), pone
 * un {@link UsernamePasswordAuthenticationToken} en el {@link SecurityContextHolder}
 * con la autoridad del rol contenido en el JWT. Spring Security usará ese contexto
 * para autorizar la petición.</p>
 *
 * <p>Es un {@link OncePerRequestFilter} para garantizar que se ejecuta una sola vez
 * por request aunque haya forwards/includes.</p>
 *
 * <p><b>Auditoría 2026-04 — cambios:</b></p>
 * <ul>
 *   <li>{@code e.printStackTrace()} sustituido por {@link Logger}. Ahora el nivel
 *       y el contexto se loguean correctamente.</li>
 *   <li>Captura específica de {@link UsernameNotFoundException} para distinguir
 *       el caso "token válido pero usuario borrado" del genérico.</li>
 * </ul>
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String JWT_COOKIE_NAME = "JWT";
    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRevocationService tokenRevocationService;

    /**
     * Procesa la petición: extrae cookie JWT, valida, y si todo OK pone el
     * {@code Authentication} en el contexto.
     *
     * @param request     petición entrante.
     * @param response    respuesta saliente.
     * @param filterChain cadena de filtros para continuar.
     * @throws ServletException error de servlet.
     * @throws IOException      error de E/S.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromCookies(request.getCookies());

        // Sin cookie o token revocado: dejamos pasar sin autenticar (otros filtros
        // bloquearán si la ruta lo requiere).
        if (token == null || tokenRevocationService.isTokenRevoked(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Si el token ha expirado, devolvemos 401 explícito en vez de seguir
            // y dejar que la ruta protegida devuelva 403/redirect a login.
            if (jwtTokenUtil.isTokenExpired(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                return;
            }

            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String role = jwtTokenUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, AuthorityUtils.createAuthorityList(role));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (UsernameNotFoundException ex) {
            // Token válido pero el usuario referenciado ya no existe (borrado entre
            // emisión y uso). No autenticamos; la cookie quedará obsoleta.
            log.warn("JWT con usuario inexistente: {}", ex.getMessage());
        } catch (Exception ex) {
            // Cualquier otra excepción no debe romper el filtro (no autenticamos).
            log.error("Error al procesar el JWT", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el valor de la cookie {@code JWT} del array de cookies de la request.
     *
     * @param cookies array de cookies (puede ser {@code null}).
     * @return el valor del token, o {@code null} si no hay cookie JWT.
     */
    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
