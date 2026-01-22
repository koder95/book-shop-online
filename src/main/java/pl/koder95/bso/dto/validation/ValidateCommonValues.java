package pl.koder95.bso.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommonValueValidator.class)
@Documented
public @interface ValidateCommonValues {

    String message() default "Fields must have the same value";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
