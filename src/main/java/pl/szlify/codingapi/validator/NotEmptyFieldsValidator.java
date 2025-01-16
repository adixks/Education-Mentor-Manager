package pl.szlify.codingapi.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import pl.szlify.codingapi.exceptions.BadInputException;

import java.util.List;
import java.util.Objects;

public class NotEmptyFieldsValidator implements ConstraintValidator<NotEmptyFields, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            throw new BadInputException();
        }
        if (value instanceof String) {
            if (((String) value).trim().isEmpty()) {
                throw new BadInputException("Wrong String");
            }
        }
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (list.stream().noneMatch(Objects::nonNull)) {
                throw new BadInputException("Wrong List");
            }
        }
        return true;
    }
}
