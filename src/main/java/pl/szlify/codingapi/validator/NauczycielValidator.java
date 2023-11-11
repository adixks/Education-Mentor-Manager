package pl.szlify.codingapi.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.szlify.codingapi.model.TeacherDto;

public class NauczycielValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherDto teacherDto = (TeacherDto) target;
        ValidationUtils.rejectIfEmpty(errors, "languagesList", "languagesList.empty");
    }
}

