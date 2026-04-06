package com.badminton.store.validation.impl.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordStrongValidator.class)
@Documented
public @interface PasswordStrong {
    String message() default "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}