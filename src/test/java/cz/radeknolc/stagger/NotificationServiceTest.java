package cz.radeknolc.stagger;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.NotificationState;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.service.NotificationService;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    private User normalUser, adminUser;

    @BeforeEach
    public void beforeEach() {
        normalUser = userRepository.findByUsername("user").get();
        adminUser = userRepository.findByUsername("admin").get();
    }

    @Test
    public void readNotification_ReadOwnNotification_True() {
        try (MockedStatic<AuthenticationUtils> authenticationUtils = Mockito.mockStatic(AuthenticationUtils.class)) {
            authenticationUtils.when(AuthenticationUtils::getLoggedUser).thenReturn(normalUser);

            assertTrue(notificationService.readNotification(2));
        }
    }

    @Test
    public void readNotification_ReadOthersNotification_False() {
        try (MockedStatic<AuthenticationUtils> authenticationUtils = Mockito.mockStatic(AuthenticationUtils.class)) {
            authenticationUtils.when(AuthenticationUtils::getLoggedUser).thenReturn(adminUser);

            assertFalse(notificationService.readNotification(2));
        }
    }

    @Test
    public void createNotification_ValidInput_Notification() {
        Notification notification = new Notification(NotificationState.PRIMARY, "Notification title", "Description", "technology-2");

        notification = notificationService.createNotification(notification, 1);
        assertNotNull(notification);
        assertTrue(notification.getId() > 0);
        assertEquals(NotificationState.PRIMARY, notification.getState());
        assertEquals("technology-2", notification.getIcon());
        assertNotNull(notification.getTitle());
        assertNotNull(notification.getDescription());
        assertFalse(notification.isRead());
    }

    @Test
    public void createNotification_NotExistingReceiver_Null() {
        Notification notification = new Notification(NotificationState.PRIMARY, "Notification title", "Description", "technology-2");

        notification = notificationService.createNotification(notification, 0);
        assertNull(notification);
    }

    @Test
    public void getUserNotifications_ValidInput_List() {
        List<Notification> notifications = notificationService.getUserNotifications(normalUser.getId());
        assertNotNull(notifications);
        assertEquals(3, notifications.size());
    }

    @Test
    public void getUserNotifications_NotExistingUser_List() {
        List<Notification> notifications = notificationService.getUserNotifications(0);
        assertNotNull(notifications);
        assertEquals(0, notifications.size());
    }
}
