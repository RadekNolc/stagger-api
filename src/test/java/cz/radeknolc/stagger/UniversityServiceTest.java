package cz.radeknolc.stagger;

import cz.radeknolc.stagger.model.University;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.UserUniversity;
import cz.radeknolc.stagger.model.request.CreateUniversityRequest;
import cz.radeknolc.stagger.repository.UniversityRepository;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.service.UniversityService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@Transactional
public class UniversityServiceTest {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UniversityService universityService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void getAllUniversities_ValidRequest_List() {
        List<University> universities = universityService.getAllUniversities();
        assertEquals(2, universities.size());
    }

    @Test
    public void getUserUniversities_ValidInput_List() {
        List<University> universities = universityService.getUserUniversities(2);
        assertEquals(1, universities.size());
    }

    @Test
    public void getUserUniversities_NotExistingUser_List() {
        List<University> universities = universityService.getUserUniversities(0);
        assertEquals(0, universities.size());
    }

    @Test
    public void createUniversity_ValidUniversityCreate_University() {
        CreateUniversityRequest createUniversityRequest = new CreateUniversityRequest("Not existing university");

        University createdUniversity = universityService.createUniversity(createUniversityRequest);
        assertNotNull(createdUniversity);
        assertTrue(createdUniversity.getId() > 0);
    }

    @Test
    public void assignUser_ValidInput_True() {
        long userId = 2;
        long universityId = 2;

        User user = userRepository.findById(userId).get();
        University university = universityRepository.findById(universityId).get();
        UserUniversity expected = new UserUniversity(user, university);

        assertEquals(1, user.getUniversities().size());
        assertFalse(user.getUniversities().contains(expected));

        boolean result = universityService.assignUser(universityId, userId);
        assertTrue(result);
        assertEquals(2, user.getUniversities().size());
        assertTrue(user.getUniversities().contains(expected));
    }

    @Test
    public void assignUser_NotExistingUser_False() {
        assertFalse(universityService.assignUser(1, 0));
    }

    @Test
    public void assignUser_NotExistingUniversity_False() {
        assertFalse(universityService.assignUser(0, 1));
    }

    @Test
    public void assignUser_AlreadyAssignedUniversity_False() {
        assertFalse(universityService.assignUser(1, 2));
    }

    @Test
    public void dismissUser_ValidInput_True() {
        long userId = 2;
        long universityId = 1;

        User user = userRepository.findById(userId).get();
        University university = universityRepository.findById(universityId).get();
        UserUniversity expected = new UserUniversity(user, university);

        assertEquals(1, user.getUniversities().size());
        assertTrue(user.getUniversities().contains(expected));

        boolean result = universityService.dismissUser(universityId, userId);
        assertTrue(result);
        assertEquals(0, user.getUniversities().size());
        assertFalse(user.getUniversities().contains(expected));
    }

    @Test
    public void dismissUser_NotExistingUser_False() {
        assertFalse(universityService.dismissUser(1, 0));
    }

    @Test
    public void dismissUser_NotExistingUniversity_False() {
        assertFalse(universityService.dismissUser(0, 1));
    }

    @Test
    public void dismissUser_NotAssignedUniversity_False() {
        assertFalse(universityService.dismissUser(2, 2));
    }
}
