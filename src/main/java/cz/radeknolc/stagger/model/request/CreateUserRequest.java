package cz.radeknolc.stagger.model.request;

import cz.radeknolc.stagger.annotation.Unique;
import cz.radeknolc.stagger.model.User;
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
    @Unique(entityClass = User.class, targetColumn = "username", message = "UNIQUE")
    private String username;
    @NotBlank(message = "NOT_BLANK")
    private String password;
    @NotBlank(message = "NOT_BLANK")
    @Email(message = "EMAIL")
    @Unique(entityClass = User.class, targetColumn = "email_address", message = "UNIQUE")
    private String email;
}
