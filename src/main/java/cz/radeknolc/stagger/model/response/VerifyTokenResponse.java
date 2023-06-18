package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyTokenResponse {

    private long id;
    private String username;
    private String emailAddress;

    public VerifyTokenResponse(User user) {
        id = user.getId();
        username = user.getUsername();
        emailAddress = user.getEmailAddress();
    }
}
