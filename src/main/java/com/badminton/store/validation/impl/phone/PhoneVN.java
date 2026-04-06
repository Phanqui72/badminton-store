package com.badminton.store.validation.impl.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneVNValidator.class)
@Documented
public @interface PhoneVN {
    String message() default "Phone number must be a valid Vietnam format (starting with 0 or +84, followed by 9 digits)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}