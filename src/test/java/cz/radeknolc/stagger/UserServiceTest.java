package cz.radeknolc.stagger;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void registerUser_ValidInput_User() {
        final int originalUsersCount = userRepository.findAll().size();
        CreateUserRequest request = new CreateUserRequest("user01", "user01", "user01@stagger.cz");
        User user = userService.registerUser(request);

        assertTrue(user.getId() > 0);
        assertNotEquals("user01", user.getPassword());
        assertEquals(1, user.getRoles().size());
        assertEquals(originalUsersCount + 1, userRepository.findAll().size());
    }
}
