package org.proyecto.fastdeliveryp_v1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.service.TokenRevocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

/**
 * Filtro que se ejecuta una vez por cada solicitud para autenticar las peticiones usando JWT.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRevocationService tokenRevocationService;

    /**
     * Filtra cada solicitud HTTP para comprobar la presencia y validez de un token JWT.
     *
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @param filterChain la cadena de filtros.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = getTokenFromCookies(request.getCookies());

        if (token != null && !tokenRevocationService.isTokenRevoked(token)) { // Verificar si el token está revocado
            try {
                if (jwtTokenUtil.isTokenExpired(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
                    return;
                }

                String username = jwtTokenUtil.getUsernameFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    String role = jwtTokenUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, AuthorityUtils.createAuthorityList(role));
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Obtiene el token JWT de las cookies de la solicitud.
     *
     * @param cookies el array de cookies de la solicitud.
     * @return el token JWT si está presente, de lo contrario null.
     */
    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("JWT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}

