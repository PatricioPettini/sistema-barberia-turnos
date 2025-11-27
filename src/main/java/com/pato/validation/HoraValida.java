package com.pato.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HoraValidaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HoraValida {

    String message() default "La hora debe ser en punto o y media (ej: 10:00 o 10:30)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}