package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.Role;
import cz.radeknolc.stagger.model.RoleName;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.UserRole;
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

    public int addCoins(long userId, int coins) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || coins < 0) {
            return -1;
        }

        int updatedCoins = user.get().getCoins() + coins;
        if (updatedCoins > user.get().getMaxCoins()) {
            updatedCoins = user.get().getMaxCoins();
        }

        user.get().setCoins(updatedCoins);
        userRepository.save(user.get());
        return updatedCoins;
    }

    public int removeCoins(long userId, int coins) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || coins < 0) {
            return -1;
        }

        int updatedCoins = user.get().getCoins() - coins;
        if (updatedCoins < 0) {
            updatedCoins = 0;
        }

        user.get().setCoins(updatedCoins);
        userRepository.save(user.get());
        return updatedCoins;
    }

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
        user.setRoles(roles);
        user.setEnabled(true);
        user = userRepository.save(user);

        if (user.getId() > 0)
            return user;

        throw new BadCredentialsException("User was not created.");
    }
}
