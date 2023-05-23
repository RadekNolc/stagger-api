 package cz.radeknolc.stagger.model;

 import org.springframework.security.authentication.AnonymousAuthenticationToken;
 import org.springframework.security.core.GrantedAuthority;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.security.core.userdetails.UserDetails;

 import java.io.Serial;
 import java.util.Collection;
 import java.util.Set;
 import java.util.stream.Collectors;

 public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = -1657293349120079617L;
    private final User user;

    public UserDetailsImpl(Long id, String username, String password, String email, String phoneNumber, boolean isActive, Set<UserRole> roles, Set<UserUniversity> universities, Set<Notification> notifications) {
        this.user = new User(id, username, password, email, phoneNumber, isActive, roles, universities, notifications);
    }

    public UserDetailsImpl(User user) {
        this(user.getId(), user.getUsername(), user.getPassword(), user.getEmailAddress(), user.getPhoneNumber(), user.getIsActive(), user.getRoles(), user.getUniversities(), user.getNotifications());
    }

    public static UserDetailsImpl getLoggedUser() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return null;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }
}
