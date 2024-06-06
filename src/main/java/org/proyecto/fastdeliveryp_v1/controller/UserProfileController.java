package org.proyecto.fastdeliveryp_v1.controller;

import org.proyecto.fastdeliveryp_v1.entity.Persona;
import org.proyecto.fastdeliveryp_v1.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    public String viewProfile(Model model) {
        Persona persona = personaService.getCurrentUser();
        model.addAttribute("persona", persona);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        Persona persona = personaService.getCurrentUser();
        model.addAttribute("persona", persona);
        return "profile/edit :: edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute Persona persona, Model model) {
        try {
            personaService.updatePersona(persona);
            model.addAttribute("success", "Perfil actualizado con éxito.");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
            return "profile/edit :: edit";
        }
    }
}