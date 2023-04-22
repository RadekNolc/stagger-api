package cz.radeknolc.stagger.model.auth;

import cz.radeknolc.stagger.model.util.TextLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String token;
    private String username;
    private List<String> roles;
    private TextLanguage language;
}
