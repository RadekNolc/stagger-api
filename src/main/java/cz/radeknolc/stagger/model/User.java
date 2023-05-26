package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1430053388500676755L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String emailAddress;
    private String phoneNumber;
    private boolean isEnabled;
    private boolean isExpired;
    private boolean isLocked;
    private boolean isCredentialsExpired;
    @OneToMany(targetEntity = UserRole.class, fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<UserRole> roles;
    @OneToMany(targetEntity = UserUniversity.class, fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    private Set<UserUniversity> universities;
    @OneToMany(targetEntity = Notification.class, fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")
    @JsonIgnore
    private Set<Notification> notifications;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User target)) return false;
        if (!getUsername().equals(target.getUsername())) return false;
        return getEmailAddress().equals(target.getEmailAddress());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(UserRole::getRole).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }
}
