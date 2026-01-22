package pl.koder95.bso.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CommonValueValidator implements ConstraintValidator<ValidateCommonValues, Object> {
    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext constraintValidatorContext) {
        try {
            List<Field> fields = Arrays.stream(bean.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(CommonValue.class))
                    .toList();

            if (fields.size() < 2) {
                return true; // nic do porównywania
            }

            Object reference = null;

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(bean);

                if (reference == null) {
                    reference = value;
                } else if (!Objects.equals(reference, value)) {
                    return false;
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
