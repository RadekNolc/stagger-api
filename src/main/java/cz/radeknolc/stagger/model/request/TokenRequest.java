package cz.radeknolc.stagger.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    @NotBlank(message = "NOT_BLANK")
    private String username;
    @NotBlank(message = "NOT_BLANK")
    private String password;
}
