package pl.szlify.codingapi.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotDateInPastValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotDateInPast {
    String message() default "{Not Date In Past}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
