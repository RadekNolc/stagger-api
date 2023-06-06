package cz.radeknolc.stagger.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedTokenResponse extends TokenResponse {

    private long id;
    private String username;
    private String emailAddress;
    private List<String> roles;

    public ExtendedTokenResponse(String token, long id, String username, String emailAddress, List<String> roles) {
        super(token);
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
        this.roles = roles;
    }
}
