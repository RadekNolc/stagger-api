package cz.radeknolc.stagger;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.NotificationIcon;
import cz.radeknolc.stagger.model.NotificationType;
import cz.radeknolc.stagger.repository.NotificationRepository;
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

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void testReadNotification() {
        try (MockedStatic<AuthenticationUtils> uDetails = Mockito.mockStatic(AuthenticationUtils.class)) {
            uDetails.when(AuthenticationUtils::getLoggedUser).thenReturn(userRepository.findByUsername("user").get());

            // Reading own notification
            int notifications = notificationService.getUserNotifications(AuthenticationUtils.getLoggedUser().getId()).size();
            assertTrue(notificationService.readNotification(2L));
            assertEquals(notifications - 1, notificationService.getUserNotifications(AuthenticationUtils.getLoggedUser().getId()).size());

            // Reading other's notification
            notifications = notificationService.getUserNotifications(1L).size();
            assertFalse(notificationService.readNotification(3L));
            assertEquals(notifications, notificationService.getUserNotifications(1L).size()); // checking unchanged state
        }
    }

    @Test
    public void testCreateNotification() {
        Notification newNotification = new Notification(NotificationType.PRIMARY, NotificationIcon.ACTIVITY, "TEST_NOTIFI");

        assertNull(newNotification.getUser());
        newNotification = notificationService.createNotification(newNotification, 1L); // sending notification to admin user

        assertTrue(newNotification.getId() > 0);
        assertEquals(1L, newNotification.getUser().getId());
    }
}
