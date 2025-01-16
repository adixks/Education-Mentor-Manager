package pl.szlify.codingapi.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherLanguagesDto {
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "LIST_TOO_SMALL")
    private List<String> languagesList;
}
