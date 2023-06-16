package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long id;
    private String username;
    private String emailAddress;
    private String phoneNumber;

    public UserResponse(User user) {
        id = user.getId();
        username = user.getUsername();
        emailAddress = user.getEmailAddress();
        phoneNumber = user.getPhoneNumber();
    }

    public static List<UserResponse> parseList(List<User> users) {
        List<UserResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserResponse(user));
        }

        return result;
    }
}
