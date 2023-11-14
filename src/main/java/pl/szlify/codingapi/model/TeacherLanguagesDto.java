package pl.szlify.codingapi.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherLanguagesDto {
    @NotEmpty
    private List<String> languagesList;
}
