package pl.koder95.bso.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Class<?> valueClass = value.getClass();
            FieldMatch match = valueClass.getAnnotation(FieldMatch.class);
            Field first = valueClass.getField(match.first());
            Field second = valueClass.getField(match.second());
            first.setAccessible(true);
            second.setAccessible(true);
            return Objects.equals(first.get(value), second.get(value));
        } catch (ReflectiveOperationException | NullPointerException ex) {
            return false;
        }
    }
}
