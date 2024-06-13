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

    /**
     * Maneja las solicitudes GET para ver el perfil del usuario actual.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista del perfil del usuario.
     */
    @GetMapping
    public String viewProfile(Model model) {
        Persona persona = personaService.getCurrentUser();
        model.addAttribute("persona", persona);
        return "profile/view";
    }

    /**
     * Maneja las solicitudes GET para editar el perfil del usuario actual.
     *
     * @param model El modelo para pasar datos a la vista.
     * @return la vista del formulario de edición de perfil.
     */
    @GetMapping("/edit")
    public String editProfile(Model model) {
        Persona persona = personaService.getCurrentUser();
        model.addAttribute("persona", persona);
        return "profile/edit :: edit";
    }

    /**
     * Maneja las solicitudes POST para actualizar el perfil del usuario actual.
     *
     * @param persona El objeto Persona con los datos actualizados.
     * @param model   El modelo para pasar datos a la vista.
     * @return la redirección a la vista del perfil del usuario o la vista de edición en caso de error.
     */
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