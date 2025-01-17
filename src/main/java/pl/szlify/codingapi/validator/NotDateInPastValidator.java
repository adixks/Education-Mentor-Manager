package pl.szlify.codingapi.validator;

import pl.szlify.codingapi.exceptions.BadInputException;
import pl.szlify.codingapi.exceptions.LessonInPastException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotDateInPastValidator implements ConstraintValidator<NotDateInPast, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            throw new BadInputException("Value cannot be null");
        }
        if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            if (localDateTime.isBefore(LocalDateTime.now())) {
                throw new LessonInPastException();
            }
        }
        return true;
    }
}
