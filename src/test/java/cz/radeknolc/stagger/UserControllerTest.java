package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User normalUser, adminUser;

    @BeforeEach
    public void beforeEach() {
        normalUser = userRepository.findByUsername("user").get();
        adminUser = userRepository.findByUsername("admin").get();
    }

    @Test
    public void userProfile_ValidRequest_OkStatusWithUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").param("userId", String.valueOf(normalUser.getId())).with(user(normalUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("GENERAL_OK"))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.roles").isArray())
                .andExpect(jsonPath("$.content.roles.length()").value(1))
                .andExpect(jsonPath("$.content.roles[?(@ == 'ROLE_USER')]").exists())
                .andExpect(jsonPath("$.content.coins").value(15))
                .andExpect(jsonPath("$.content.maxCoins").value(20))
                .andExpect(jsonPath("$.content.universityCount").value(1));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").param("userId", String.valueOf(adminUser.getId())).with(user(adminUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("GENERAL_OK"))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.roles").isArray())
                .andExpect(jsonPath("$.content.roles.length()").value(1))
                .andExpect(jsonPath("$.content.roles[?(@ == 'ROLE_ADMIN')]").exists())
                .andExpect(jsonPath("$.content.coins").value(8))
                .andExpect(jsonPath("$.content.maxCoins").value(20))
                .andExpect(jsonPath("$.content.universityCount").value(0));
    }

    @Test
    public void userProfile_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").param("userId", String.valueOf(normalUser.getId())).with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userProfile_InvalidUserIdRequest_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").param("userId", String.valueOf(adminUser.getId())).with(user(normalUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("AUTH_USER_VERIFY_ERROR"));
    }

    @Test
    public void registerUser_ValidInput_CreatedStatusWithMessageAndUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("register", "register", "register@stagger.cz"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("USER_REGISTRATION_SUCCESS"))
                .andExpect(jsonPath("$.content.username").value("register"))
                .andExpect(jsonPath("$.content.password").doesNotExist())
                .andExpect(jsonPath("$.content.emailAddress").value("register@stagger.cz"));
    }

    @Test
    public void registerUser_AlreadyRegisteredUsername_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("duplicate", "duplicate", "duplicate@stagger.cz"))))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("duplicate", "xxxxxx", "xxxxx@stagger.cz"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.username").value("UNIQUE"));
    }

    @Test
    public void registerUser_AlreadyRegisteredEmail_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("duplicate", "duplicate", "duplicate@stagger.cz"))))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("xxxxxx", "xxxxxx", "duplicate@stagger.cz"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.email").value("UNIQUE"));
    }

    @Test
    public void registerUser_InvalidEmailFormat_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUserRequest("invalidemail", "invalidemail", "invalidemail"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.email").value("EMAIL"));
    }
}
