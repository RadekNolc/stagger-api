package cz.radeknolc.stagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUniversityRequest;
import cz.radeknolc.stagger.model.request.UserAssignUniversityRequest;
import cz.radeknolc.stagger.model.request.UserDismissUniversity;
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
public class UniversityControllerTest {

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
    public void createUniversity_ValidInput_OkStatusWithMessageAndUniversity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/create").with(user(adminUser)).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUniversityRequest("Karlova univerzita"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("UNIVERSITY_CREATED"));
    }

    @Test
    public void createUniversity_AlreadyExistingUniversity_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/create").with(user(adminUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUniversityRequest("Západočeská univerzita"))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.name").value("UNIQUE"));
    }

    @Test
    public void createUniversity_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/create").with(anonymous()).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateUniversityRequest("Jihočeská univerzita"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void allUniversities_ValidRequest_OkStatusWithUniversityList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/university/all").with(user(normalUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void allUniversities_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/university/all").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userUniversities_ValidRequest_OkStatusWithUniversityList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/university/user").with(user(normalUser)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void userUniversities_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/university/user").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userAssignUniversity_ValidRequest_OkStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/assign").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserAssignUniversityRequest(2))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("UNIVERSITY_ASSIGNED"));
    }

    @Test
    public void userAssignUniversity_AlreadyAssignedUniversity_ClientErrorStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/assign").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserAssignUniversityRequest(1))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("UNIVERSITY_NOT_ASSIGNED"));
    }

    @Test
    public void userAssignUniversity_NotExistingUniversity_ClientErrorStatusWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/assign").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserAssignUniversityRequest(0))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.universityId").value("NOT_EXISTS"));
    }

    @Test
    public void userAssignUniversity_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/assign").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void userDismissUniversity_ValidRequest_OkStatusWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/dismiss").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDismissUniversity(1))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("UNIVERSITY_DISMISSED"));
    }

    @Test
    public void userDismissUniversity_NotAssignedUniversity_ClientErrorWithMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/dismiss").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDismissUniversity(2))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("UNIVERSITY_NOT_DISMISSED"));
    }

    @Test
    public void userDismissUniversity_NotExistingUniversity_ClientErrorWithMessageAndValidationErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/dismiss").with(user(normalUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDismissUniversity(0))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.content.universityId").value("NOT_EXISTS"));
    }

    @Test
    public void userDismissUniversity_NotAuthenticated_UnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/university/dismiss").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }
}
