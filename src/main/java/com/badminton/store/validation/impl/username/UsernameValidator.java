package com.badminton.store.validation.impl.username;

import com.badminton.store.constant.MgrConstant;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true; // Để @NotEmpty xử lý
        return value.matches(MgrConstant.REGEX_USERNAME);
    }
}