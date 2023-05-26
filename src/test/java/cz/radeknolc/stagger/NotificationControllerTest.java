package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.ReadNotificationRequest;
import cz.radeknolc.stagger.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void testReadNotificationSuccess() throws Exception {
        Optional<User> user = userRepository.findByUsername("user");

        if (user.isPresent()) {
            mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(user.get()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReadNotificationRequest(1L))))
                    .andExpect(status().isOk());

            mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(user.get()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReadNotificationRequest(2L))))
                    .andExpect(status().isOk());
        } else {
            throw new UsernameNotFoundException("Called user does not exist.");
        }
    }

    @Test
    public void testReadNotificationError() throws Exception {
        Optional<User> user = userRepository.findByUsername("admin");

        if (user.isPresent()) {
            mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(user.get()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReadNotificationRequest(1L))))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(MockMvcRequestBuilders.post("/notification/read").with(user(user.get()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new ReadNotificationRequest(2L))))
                    .andExpect(status().isUnauthorized());
        } else {
            throw new UsernameNotFoundException("Called user does not exist.");
        }
    }
}
