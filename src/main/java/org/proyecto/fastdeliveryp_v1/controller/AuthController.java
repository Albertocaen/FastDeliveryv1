package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.security.JwtTokenUtil;
import org.proyecto.fastdeliveryp_v1.service.PersonaService;
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

import java.util.Collection;

@Controller

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PersonaService personaService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

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

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

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
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Eliminar la cookie JWT
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Set the cookie to expire immediately
        response.addCookie(cookie);

        // Invalidate the session
        request.getSession().invalidate();

        return "redirect:/inicio";
    }
}


