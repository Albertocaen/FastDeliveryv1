package org.proyecto.fastdeliveryp_v1.interfacE;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.proyecto.fastdeliveryp_v1.classes.NumericValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumericValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Numeric {
    String message() default "El teléfono solo puede contener números";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
