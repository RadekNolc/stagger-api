package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String username;
    @JsonIgnore
    private String password;
    private String emailAddress;
    private String phoneNumber;
    private boolean isActive;

    public User(int id, String username, String password, String emailAddress, String phoneNumber, boolean isActive) {
        super(id);
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }
}
