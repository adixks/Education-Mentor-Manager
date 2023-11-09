package pl.szlify.codingapi.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.szlify.codingapi.model.NauczycielModel;

public class NauczycielValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NauczycielModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NauczycielModel nauczyciel = (NauczycielModel) target;
        ValidationUtils.rejectIfEmpty(errors, "listaJezykow", "listaJezykow.empty");
    }
}

