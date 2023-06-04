package cz.radeknolc.stagger;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.NotificationIcon;
import cz.radeknolc.stagger.model.NotificationType;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.service.NotificationService;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
@Transactional
public class NotificationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void readNotification_ReadOwnNotification_True() {
        Optional<User> user = userRepository.findByUsername("user");

        if (user.isPresent()) {
            try (MockedStatic<AuthenticationUtils> authenticationUtils = Mockito.mockStatic(AuthenticationUtils.class)) {
                authenticationUtils.when(AuthenticationUtils::getLoggedUser).thenReturn(user.get());

                assertTrue(notificationService.readNotification(2L));
            }
        } else {
            throw new UsernameNotFoundException("Called user does not exist.");
        }
    }

    @Test
    public void readNotification_ReadOthersNotification_False() {
        Optional<User> user = userRepository.findByUsername("admin");

        if (user.isPresent()) {
            try (MockedStatic<AuthenticationUtils> authenticationUtils = Mockito.mockStatic(AuthenticationUtils.class)) {
                authenticationUtils.when(AuthenticationUtils::getLoggedUser).thenReturn(user.get());

                assertFalse(notificationService.readNotification(2L));
            }
        } else {
            throw new UsernameNotFoundException("Called user does not exist.");
        }
    }

    @Test
    public void createNotification_ValidInput_Notification() {
        Notification newNotification = new Notification(NotificationType.PRIMARY, NotificationIcon.ACTIVITY, "TEST_NOTIFICATION");

        newNotification = notificationService.createNotification(newNotification, 1L);
        assertNotNull(newNotification);
        assertTrue(newNotification.getId() > 0);
        assertEquals(NotificationType.PRIMARY, newNotification.getType());
        assertEquals(NotificationIcon.ACTIVITY, newNotification.getIcon());
        assertNotNull(newNotification.getMessage());
        assertFalse(newNotification.isRead());
    }

    @Test
    public void createNotification_NotExistingReceiver_Null() {
        Notification newNotification = new Notification(NotificationType.PRIMARY, NotificationIcon.ACTIVITY, "TEST_NOTIFICATION");

        newNotification = notificationService.createNotification(newNotification, 1000000L);
        assertNull(newNotification);
    }
}
