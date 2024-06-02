package org.proyecto.fastdeliveryp_v1.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.repository.PersonaRepository;
import org.proyecto.fastdeliveryp_v1.security.JwtTokenUtil;
import org.proyecto.fastdeliveryp_v1.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgotPassword")
    public String forgotPasswordPage() {
        return "clientes/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email, Model model) {
        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            model.addAttribute("message", "No se encontró una cuenta con ese correo electrónico.");
            return "clientes/forgotPassword";
        }

        String token = jwtTokenUtil.createPasswordResetToken(email);
        String resetUrl = "http://localhost:8081/auth/resetPassword?token=" + token;
        emailService.sendEmail(email, "Restablecer Contraseña", "Para restablecer tu contraseña, haz clic en el siguiente enlace: " + resetUrl);

        model.addAttribute("message", "Se ha enviado un correo electrónico con instrucciones para restablecer la contraseña.");
        return "clientes/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordPage(@RequestParam String token, Model model, HttpServletResponse response) {
        // Almacenar el token en una cookie
        Cookie cookie = new Cookie("RESET_TOKEN", token);
        cookie.setHttpOnly(true); // Hacer la cookie HTTP-Only
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 24 horas
        response.addCookie(cookie);

        model.addAttribute("token", token);
        return "clientes/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String newPassword, Model model, HttpServletRequest request) {
        // El token se recuperará del JwtTokenFilter
        String token = getTokenFromCookies(request.getCookies());
        if (token == null) {
            model.addAttribute("message", "Token de restablecimiento de contraseña no encontrado.");
            return "clientes/resetPassword";
        }

        String email = jwtTokenUtil.getUsernameFromPasswordResetToken(token);
        if (email == null) {
            model.addAttribute("message", "Token de restablecimiento de contraseña inválido o expirado.");
            return "clientes/resetPassword";
        }

        Persona persona = personaRepository.findByEmail(email);
        if (persona == null) {
            model.addAttribute("message", "No se encontró una cuenta con ese correo electrónico.");
            return "clientes/resetPassword";
        }

        persona.setContraseña(passwordEncoder.encode(newPassword));
        personaRepository.save(persona);

        model.addAttribute("message", "La contraseña se ha restablecido correctamente.");
        return "login";
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        String authToken = null;
        String resetToken = null;

        for (Cookie cookie : cookies) {
            if ("JWT".equals(cookie.getName())) {
                authToken = cookie.getValue();
            } else if ("RESET_TOKEN".equals(cookie.getName())) {
                resetToken = cookie.getValue();
            }
        }

        // Return authToken if present, else return resetToken
        return authToken != null ? authToken : resetToken;
    }
}

