package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return UserDetailsImpl.build(new User(1, "admin", "$2a$12$3908H4Ucw8cINvJ7nJi1meqZi4PDWwbvGvKA3loal/TVD81jpbFGm", "radek.nolc@icloud.com", "123456", true));
        }

        throw new UsernameNotFoundException("User not found with username " + username);
    }
}
