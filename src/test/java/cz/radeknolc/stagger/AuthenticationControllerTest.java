package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.LoginRequest;
import cz.radeknolc.stagger.model.request.VerifyTokenRequest;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private UserRepository userRepository;

    private User normalUser, adminUser;

    @BeforeEach
    public void beforeEach() {
        normalUser = userRepository.findByUsername("user").get();
        adminUser = userRepository.findByUsername("admin").get();
    }

    @Test
    public void authenticate_ValidCredentials_OkStatusWithToken() throws Exception {
        // Administrator authentication
        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/authenticate").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest("admin", "admin"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.emailAddress").value("admin@stagger.cz"))
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
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.emailAddress").value("user@stagger.cz"))
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

    @Test
    public void verifyToken_AuthenticatedWithValidTokenRequest_OkStatusWithUser() throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "user"));
        String token = authenticationUtils.generateToken(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/verify").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VerifyTokenRequest(token))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.username").value("user"))
                .andExpect(jsonPath("$.content.id").value(2));
    }

    @Test
    public void verifyToken_NotAuthenticatedWithValidTokenRequest_OkStatusWithUser() throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "user"));
        String token = authenticationUtils.generateToken(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/verify").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VerifyTokenRequest(token))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.username").value("user"))
                .andExpect(jsonPath("$.content.id").value(2));
    }

    @Test
    public void verifyToken_InvalidToken_ClientErrorStatusWithMessage() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/verify").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VerifyTokenRequest(token))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("AUTH_TOKEN_INVALID"));
    }

}
