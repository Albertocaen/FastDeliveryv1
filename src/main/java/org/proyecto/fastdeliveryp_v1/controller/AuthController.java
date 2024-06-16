package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.security.JwtTokenUtil;
import org.proyecto.fastdeliveryp_v1.service.PersonaService;
import org.proyecto.fastdeliveryp_v1.service.TokenRevocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoClienteController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private TokenRevocationService tokenRevocationService;


    /**
     * Muestra la página de inicio de sesión.
     *
     * @return la vista de la página de inicio de sesión.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Maneja la solicitud de inicio de sesión.
     *
     * @param email    el correo electrónico del usuario.
     * @param password la contraseña del usuario.
     * @param model    el modelo para pasar datos a la vista.
     * @param response la respuesta HTTP para agregar cookies.
     * @return la vista a redirigir después del inicio de sesión.
     */
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Obtener de manera segura la primera autoridad o usar una por defecto
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String authority = authorities.isEmpty() ? "USER" : authorities.iterator().next().getAuthority();

            // Generar el token JWT
            String token = jwtTokenUtil.createToken(email, authority);

            // Almacenar el token en una cookie
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true); // Hacer la cookie HTTP-Only
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 1 semana
            response.addCookie(cookie);

            return "redirect:/inicio"; // Redirigir a la página de clientes después del inicio de sesión exitoso
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Credenciales de inicio de sesión inválidas");
            return "login";
        }
    }

    /**
     * Muestra la página de registro.
     *
     * @return la vista de la página de registro.
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


    /**
     * Maneja la solicitud de registro.
     *
     * @param dni        el DNI del usuario.
     * @param email      el correo electrónico del usuario.
     * @param nombre     el nombre del usuario.
     * @param apellido   el apellido del usuario.
     * @param telefono   el teléfono del usuario.
     * @param contrasena la contraseña del usuario.
     * @param model      el modelo para pasar datos a la vista.
     * @return la vista a redirigir después del registro.
     */
    @PostMapping("/register")
    public String register(@RequestParam String dni,
                           @RequestParam String email,
                           @RequestParam String nombre,
                           @RequestParam String apellido,
                           @RequestParam String telefono,
                           @RequestParam String contrasena,
                           Model model) {
        try {
            personaService.registerUser(dni, email, nombre, apellido, telefono, contrasena);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error en el registro: " + e.getMessage());
            return "register";
        }
    }

    /**
     * Maneja la solicitud de cierre de sesión.
     *
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP para manipular cookies.
     * @return la vista a redirigir después del cierre de sesión.
     */

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, String domain) {
        String token = getTokenFromCookies(request.getCookies());
        if (token != null) {
            tokenRevocationService.revokeToken(token);
        }

        // Invalidar las cookies para cerrar la sesión
        invalidateCookie(response, "JWT", domain);
        invalidateCookie(response, "RESET_TOKEN", domain);

        // Invalidar la sesión HTTP
        request.getSession().invalidate();
        logger.info("Redirigiendo a /inicio");
        return "redirect:/";
    }

    /**
     * Lee y luego invalida una cookie.
     *
     *
     * @param response la respuesta HTTP para manipular cookies.
     * @param name     el nombre de la cookie a invalidar.
     */

    private void invalidateCookie(HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

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



