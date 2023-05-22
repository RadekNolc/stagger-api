package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@Transactional
@SqlGroup({
        @Sql(scripts = "/schema.sql"),
        @Sql(scripts = "/data.sql")
})
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRegistrationSuccess() throws Exception {
        int originalUsers = userRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateUserRequest("register", "register", "register@stagger.cz"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("REGISTRATION_SUCCESS"));

        Assertions.assertEquals(++originalUsers, userRepository.findAll().size()); // Checking if user was created

        Optional<User> createdUser = userRepository.findByUsername("register");
        Assertions.assertTrue(createdUser.isPresent()); // Checking if user was created
        Assertions.assertNotNull(createdUser.get().getIsActive()); // Checking if proper value was set
        Assertions.assertNotNull(createdUser.get().getCreatedAt()); // Checking if created at is not null and set
        Assertions.assertEquals(1, createdUser.get().getRoles().size()); // Checking if user got role
    }

    @Test
    public void testUserRegistrationError() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateUserRequest("duplicate", "duplicate", "duplicate@stagger.cz"))))
                .andReturn();

        // Checking for duplicate username
        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(anonymous())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateUserRequest("duplicate", "xxxxxx", "xxxxx@stagger.cz"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.username").value("UNIQUE"));

        // Invalid e-mail format
        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("invalidemail", "invalidemail", "invalidemail"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.email").value("EMAIL"));
    }
}
