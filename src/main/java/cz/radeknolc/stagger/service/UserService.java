package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.*;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.repository.RoleRepository;
import cz.radeknolc.stagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(CreateUserRequest createUserRequest) {
        Optional<Role> role = roleRepository.findByName(RoleName.USER);
        User user = new User();
        Set<UserRole> roles = new HashSet<>();

        if (role.isPresent()) {
            roles.add(new UserRole(user, role.get()));
        }

        user.setUsername(createUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setEmailAddress(createUserRequest.getEmail());
        user.setLanguage(TextLanguage.valueOf(createUserRequest.getLanguage()));
        user.setRoles(roles);
        user.setIsActive(true);
        user = userRepository.save(user);

        if (user.getId() > 0)
            return user;

        throw new BadCredentialsException("User was not created.");
    }
}
