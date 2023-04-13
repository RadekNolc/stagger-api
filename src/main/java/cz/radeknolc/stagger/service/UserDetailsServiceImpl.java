package cz.radeknolc.stagger.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new User("admin", "$2a$12$3908H4Ucw8cINvJ7nJi1meqZi4PDWwbvGvKA3loal/TVD81jpbFGm", new ArrayList<>());
        } else if ("test".equals(username)) {
            return new User("test", "$2a$12$PnErzaLgnYfWLusNu9iUvuMg8m8qkhhMCOEBCZ7yu5BD..PHeG1lK", new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found with username " + username);
    }
}
