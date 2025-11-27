package com.pato.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class HoraValidaValidator implements ConstraintValidator<HoraValida, LocalTime> {

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {

        if (value == null) {
            return true; // @NotNull lo controla en el DTO
        }

        int minutos = value.getMinute();

        // Solo permitir 00 y 30
        return minutos == 0 || minutos == 30;
    }
}
