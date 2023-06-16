package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import jakarta.transaction.Transactional;
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
