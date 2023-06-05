package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.request.LoginRequest;
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
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void authenticate_ValidCredentials_OkStatusWithToken() throws Exception {
        // Administrator authentication
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new LoginRequest("admin", "admin"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[?(@ == 'ROLE_ADMIN')]").exists());

        // User authentication
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("user", "user"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[?(@ == 'ROLE_USER')]").exists());
    }

    @Test
    public void authenticate_NotExistingUser_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("test", "123"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("AUTH_BAD_CREDENTIALS"));
    }

    @Test
    public void authenticate_BlankFields_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("", ""))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.username").value("NOT_BLANK"))
                .andExpect(jsonPath("$.content.password").value("NOT_BLANK"));
    }

    @Test
    public void authenticate_WrongCredentials_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("admin", "wrongpassword"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("AUTH_BAD_CREDENTIALS"));
    }

    @Test
    public void authenticate_DisabledAccount_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new LoginRequest("inactive", "inactive"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("AUTH_ACCOUNT_DISABLED"));
    }

    @Test
    public void authenticate_ExpiredAccount_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("expired", "expired"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("AUTH_ACCOUNT_EXPIRED"));
    }

    @Test
    public void authenticate_LockedAccount_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("locked", "locked"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("AUTH_ACCOUNT_LOCKED"));
    }

    @Test
    public void authenticate_CredentialsExpired_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("credentials_expired", "credentials_expired"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("AUTH_CREDENTIALS_EXPIRED"));
    }
}
