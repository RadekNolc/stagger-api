package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String emailAddress;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private TextLanguage language;
    private Boolean isActive;
    @OneToMany(targetEntity = UserRole.class, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserRole> roles;
    @OneToMany(targetEntity = UserUniversity.class, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserUniversity> universities;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User target)) return false;
        if (!getUsername().equals(target.getUsername())) return false;
        return getEmailAddress().equals(target.getEmailAddress());
    }
}
