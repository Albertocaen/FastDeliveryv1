package org.proyecto.fastdeliveryp_v1.classes;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.proyecto.fastdeliveryp_v1.interfacE.ImageUrl;

public class ImageUrlValidator implements ConstraintValidator<ImageUrl, String> {

    @Override
    public void initialize(ImageUrl constraintAnnotation) {
        // Inicialización si es necesario
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.endsWith(".png") || value.endsWith(".jpg");
    }
}
