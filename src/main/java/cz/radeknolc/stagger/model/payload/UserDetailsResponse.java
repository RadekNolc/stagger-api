package cz.radeknolc.stagger.model.payload;

import org.springframework.security.core.GrantedAuthority;
import cz.radeknolc.stagger.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {

    private long id;
    private String username;
    private String emailAddress;
    private String phoneNumber;
    private List<String> roles;

    public UserDetailsResponse(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.emailAddress = user.getEmailAddress();
            this.phoneNumber = user.getPhoneNumber();
            this.roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        }
    }
}
