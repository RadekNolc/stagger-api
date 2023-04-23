 package cz.radeknolc.stagger.service;

 import cz.radeknolc.stagger.model.User;
 import cz.radeknolc.stagger.model.map.UserRole;
 import cz.radeknolc.stagger.model.map.UserUniversity;
 import cz.radeknolc.stagger.model.util.TextLanguage;
 import org.springframework.security.authentication.AnonymousAuthenticationToken;
 import org.springframework.security.core.GrantedAuthority;
 import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public UserDetailsImpl(Long id, String username, String password, String email, String phoneNumber, TextLanguage language, boolean isActive, Set<UserRole> roles, Set<UserUniversity> universities) {
        this.user = new User(id, username, password, email, phoneNumber, language, isActive, roles, universities);
    }

    public TextLanguage getLanguage() {
        return user.getLanguage();
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getEmailAddress(), user.getPhoneNumber(), user.getLanguage(), user.getIsActive(), user.getRoles(), user.getUniversities());
    }

    public static UserDetailsImpl getLoggedUser() {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(roleMapper -> new SimpleGrantedAuthority("ROLE_" + roleMapper.getRole().getName())).collect(Collectors.toList());
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
