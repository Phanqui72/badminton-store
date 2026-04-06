package com.badminton.store.validation.impl.phone;

import com.badminton.store.constant.MgrConstant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneVNValidator implements ConstraintValidator<PhoneVN, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;
        return value.matches(MgrConstant.REGEX_PHONE_VN);
    }
}