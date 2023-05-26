package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.repository.NotificationRepository;
import cz.radeknolc.stagger.repository.UserRepository;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(value -> new ArrayList<>(value.getNotifications().stream().filter(Notification -> !Notification.getIsRead()).toList())).orElseGet(ArrayList::new);
    }

    public boolean readNotification(Long notificationId) {
        Optional<Notification> n = notificationRepository.findById(notificationId);
        if (n.isPresent()) {
            Notification notification = n.get();
            User currentUser = AuthenticationUtils.getLoggedUser();
            if (currentUser != null && currentUser.getId().equals(notification.getUser().getId())) {
                notification.setIsRead(true);
                notificationRepository.save(notification);
                return true;
            }
        }

        return false;
    }

    public Notification createNotification(Notification notification, Long receiverId) {
        Optional<User> receiver = userRepository.findById(receiverId);
        if (receiver.isPresent()) {
            notification.setUser(receiver.get());
            notification = notificationRepository.save(notification);
            return notification;
        }

        return null;
    }
}
