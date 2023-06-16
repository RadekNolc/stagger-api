package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private String username;
    private String emailAddress;

    public CreateUserResponse(User user) {
        username = user.getUsername();
        emailAddress = user.getEmailAddress();
    }
}
