package cz.radeknolc.stagger;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

    private User normalUser;

    @BeforeEach
    public void beforeEach() {
        normalUser = userRepository.findByUsername("user").get();
    }

    @Test
    public void addCoins_ValidInput_Integer() {
        assertEquals(15, normalUser.getCoins());
        assertEquals(18, userService.addCoins(normalUser.getId(), 3));
        assertEquals(18, normalUser.getCoins());
        assertEquals(18, userRepository.findById(normalUser.getId()).get().getCoins());
    }

    @Test
    public void addCoins_CoinOverflow_Integer() {
        assertEquals(15, normalUser.getCoins());
        assertEquals(20, userService.addCoins(normalUser.getId(), 100));
        assertEquals(20, normalUser.getCoins());
        assertEquals(20, userRepository.findById(normalUser.getId()).get().getCoins());
    }

    @Test
    public void addCoins_NegativeCoins_Integer() {
        assertEquals(-1, userService.addCoins(normalUser.getId(), -5));
        assertEquals(15, normalUser.getCoins());
    }

    @Test
    public void removeCoins_ValidInput_Integer() {
        assertEquals(15, normalUser.getCoins());
        assertEquals(12, userService.removeCoins(normalUser.getId(), 3));
        assertEquals(12, normalUser.getCoins());
        assertEquals(12, userRepository.findById(normalUser.getId()).get().getCoins());
    }

    @Test
    public void removeCoins_CoinOverflow_Integer() {
        assertEquals(15, normalUser.getCoins());
        assertEquals(0, userService.removeCoins(normalUser.getId(), 100));
        assertEquals(0, normalUser.getCoins());
        assertEquals(0, userRepository.findById(normalUser.getId()).get().getCoins());
    }

    @Test
    public void removeCoins_NegativeCoins_Integer() {
        assertEquals(-1, userService.removeCoins(normalUser.getId(), -5));
        assertEquals(15, normalUser.getCoins());
    }

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
