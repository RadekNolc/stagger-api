package cz.radeknolc.stagger.model.auth;

import cz.radeknolc.stagger.model.util.TextLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String token;
    private String username;
    private TextLanguage language;
}
