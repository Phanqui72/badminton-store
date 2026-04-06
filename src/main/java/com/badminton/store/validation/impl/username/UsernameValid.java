package com.badminton.store.validation.impl.username;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface UsernameValid {
    String message() default "Username must be between 4 and 50 characters and can only contain letters, numbers, or underscores";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}