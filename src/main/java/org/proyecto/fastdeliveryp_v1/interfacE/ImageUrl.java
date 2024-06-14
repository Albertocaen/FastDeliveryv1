package org.proyecto.fastdeliveryp_v1.interfacE;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.proyecto.fastdeliveryp_v1.classes.ImageUrlValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageUrlValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageUrl {
    String message() default "La URL debe terminar en .png o .jpg";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
