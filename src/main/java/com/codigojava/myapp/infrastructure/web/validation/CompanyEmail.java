package com.codigojava.myapp.infrastructure.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CompanyEmailValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface CompanyEmail {
    String message() default "Email debe terminar en @codigojava.net";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String domain() default "@codigojava.net";
}
