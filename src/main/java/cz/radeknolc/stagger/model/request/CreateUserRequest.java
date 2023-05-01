package cz.radeknolc.stagger.model.request;

import cz.radeknolc.stagger.annotation.ValueOfEnum;
import cz.radeknolc.stagger.model.TextLanguage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "NOT_BLANK")
    private String username;
    @NotBlank(message = "NOT_BLANK")
    private String password;
    @NotBlank(message = "NOT_BLANK")
    @Email(message = "EMAIL")
    private String email;
    @ValueOfEnum(enumClass = TextLanguage.class, message = "VALUE_OF_ENUM")
    private String language;
}
