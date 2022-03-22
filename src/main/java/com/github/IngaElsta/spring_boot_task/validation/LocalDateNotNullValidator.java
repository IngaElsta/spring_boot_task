package com.github.IngaElsta.spring_boot_task.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.temporal.TemporalAccessor;

public class LocalDateNotNullValidator implements ConstraintValidator<LocalDateNotNull, TemporalAccessor> {

    @Override
    public void initialize(LocalDateNotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(TemporalAccessor value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        return true;
    }
}
