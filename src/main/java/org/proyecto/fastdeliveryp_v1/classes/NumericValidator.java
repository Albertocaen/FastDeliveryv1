package org.proyecto.fastdeliveryp_v1.classes;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.proyecto.fastdeliveryp_v1.interfacE.Numeric;

public class NumericValidator implements ConstraintValidator<Numeric, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // No considerar null como inválido
        }
        return value.matches("\\d+");
    }
}
