package org.proyecto.fastdeliveryp_v1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración central de Spring Security.
 *
 * <p>Decisiones clave:</p>
 * <ul>
 *   <li><b>Sesión STATELESS</b> — la app es stateless desde el punto de vista
 *       de Spring Security; el estado de autenticación viaja en el JWT (cookie).
 *       Spring no crea {@code HttpSession} para guardar el SecurityContext.</li>
 *   <li><b>{@link JwtTokenFilter}</b> antes del filtro de username/password,
 *       para que cuando llegue una petición con cookie JWT, ya esté el
 *       {@code Authentication} en el contexto.</li>
 *   <li><b>{@link EnableMethodSecurity}</b> activa las anotaciones
 *       {@code @PreAuthorize} / {@code @PostAuthorize} a nivel de método.
 *       Sin esto, las anotaciones se ignoran silenciosamente.</li>
 * </ul>
 *
 * <p><b>Deuda conocida (auditoría 2026-04):</b></p>
 * <ul>
 *   <li>CSRF deshabilitado. Aceptable mientras los formularios sensibles vayan
 *       sólo desde el mismo dominio y la cookie tenga {@code SameSite=Lax}
 *       (default en Chrome moderno). Próxima iteración: habilitar
 *       {@code CookieCsrfTokenRepository.withHttpOnlyFalse()} y meter
 *       {@code th:name="${_csrf.parameterName}"} en formularios.</li>
 *   <li>{@code /templates/**} está fuera de la lista permitAll desde la
 *       auditoría — el resource handler que lo exponía ha sido eliminado en
 *       {@code WebConfig}.</li>
 *   <li>Falta rate limiting en {@code /login}, {@code /register} y
 *       {@code /auth/forgotPassword}. Próxima iteración: Bucket4j.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // habilita @PreAuthorize / @PostAuthorize en controllers y services
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    /**
     * Cadena de filtros HTTP de seguridad.
     *
     * @param http el {@link HttpSecurity} para configurar.
     * @return la {@link SecurityFilterChain} construida.
     * @throws Exception si la construcción falla.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF — ver nota de deuda en el javadoc de la clase.
            .csrf(csrf -> csrf.disable())

            // STATELESS: el JWT contiene la identidad, no usamos HttpSession para auth.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Reglas de autorización por path.
            .authorizeHttpRequests(authorize -> authorize
                // Públicos: páginas anónimas, recursos estáticos, callbacks PayPal.
                .requestMatchers(
                    "/", "/inicio", "/sobre-mi",
                    "/login", "/register",
                    "/auth/**", "/auth/forgotPassword", "/auth/resetPassword/**",
                    "/pedidos/success", "/pedidos/cancel",
                    "/static/**", "/css/**", "/js/**", "/images/**", "/uploads/**",
                    "/logout"
                ).permitAll()

                // Admin only.
                .requestMatchers("/admin/**", "/gestor", "/gestor/**").hasAuthority("ROLE_ADMIN")

                // Usuario autenticado (independiente de rol).
                .requestMatchers("/user/**", "/carrito/**", "/perfil/**", "/profile/**").authenticated()

                // Resto: cualquier autenticado, los @PreAuthorize de cada controller
                // refinan el control por rol.
                .anyRequest().authenticated()
            )

            // Filtro JWT antes del filtro estándar de username/password.
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // Logout deshabilitado: hay un endpoint custom que invalida la cookie
        // y añade el token a TokenRevocationService.
        http.logout(logout -> logout.disable());

        return http.build();
    }

    /**
     * {@link AuthenticationManager} expuesto para que el endpoint de login pueda
     * delegar la autenticación.
     *
     * @param authenticationConfiguration la configuración por defecto.
     * @return el manager.
     * @throws Exception si la obtención falla.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Handler por defecto para 403. Genera la respuesta estándar; el
     * {@code GlobalExceptionHandler} lo refina si la excepción se propaga al
     * controller.
     *
     * @return el handler.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    /**
     * Codificador de contraseñas. BCrypt con coste por defecto (10).
     *
     * <p>El prefijo de hash es {@code $2a$}, que es lo que valida
     * {@link PasswordEncryptor} para detectar contraseñas ya cifradas.</p>
     *
     * @return el encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
