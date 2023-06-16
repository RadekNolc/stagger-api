package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyTokenResponse {

    private long id;
    private String username;
    private String emailAddress;
    private String phoneNumber;
    private List<String> roles;

    public VerifyTokenResponse(User user) {
        id = user.getId();
        username = user.getUsername();
        emailAddress = user.getEmailAddress();
        phoneNumber = user.getPhoneNumber();
        roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
