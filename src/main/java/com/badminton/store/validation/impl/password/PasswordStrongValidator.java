package com.badminton.store.validation.impl.password;

import com.badminton.store.constant.MgrConstant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordStrongValidator implements ConstraintValidator<PasswordStrong, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;
        return value.matches(MgrConstant.REGEX_PASSWORD);
    }
}