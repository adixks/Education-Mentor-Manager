package pl.szlify.codingapi.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherLanguagesDto {
    @NotNull(message = "NULL_VALUE")
    @Size(min = 2, message = "LIST_TOO_SMALL")
    private List<String> languagesList;
}
