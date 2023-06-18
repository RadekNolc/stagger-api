package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.ReadNotificationRequest;
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
public class NotificationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User normalUser, adminUser;

    @BeforeEach
    public void beforeEach() {
        normalUser = userRepository.findByUsername("user").get();
        adminUser = userRepository.findByUsername("admin").get();
    }

    @Test
    public void readNotification_ReadOwnNotification_OkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReadNotificationRequest(1))))
                .andExpect(status().isOk());
    }

    @Test
    public void readNotification_ReadOthersNotification_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(adminUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReadNotificationRequest(1))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void readNotification_EmptyNotificationId_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReadNotificationRequest())))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.notificationId").value("NOT_EXISTS"));
    }

    @Test
    public void readNotification_NotExistingNotification_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReadNotificationRequest(0))))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.notificationId").value("NOT_EXISTS"));
    }

    @Test
    public void readNotification_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReadNotificationRequest(1))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userNotifications_ValidRequest_OkStatusWithNotificationList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/user").param("userId", String.valueOf(normalUser.getId())).with(user(normalUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    public void userNotifications_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/user").param("userId", String.valueOf(normalUser.getId())).with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userNotifications_InvalidUserIdRequest_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notification/user").param("userId", String.valueOf(adminUser.getId())).with(user(normalUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("AUTH_USER_VERIFY_ERROR"));
    }
}
