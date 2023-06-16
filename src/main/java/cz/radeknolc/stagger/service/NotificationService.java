package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.repository.NotificationRepository;
import cz.radeknolc.stagger.repository.UserRepository;
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

    public List<Notification> getUserNotifications(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(value -> new ArrayList<>(value.getNotifications().stream().filter(Notification -> !Notification.isRead()).toList())).orElseGet(ArrayList::new);
    }

    public boolean readNotification(long notificationId, long userId) {
        boolean result = false;
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty() || notification.isEmpty()) {
            return result;
        }

        if (!notification.get().getUser().equals(user.get())) {
            return result;
        }

        result = true;
        notification.get().setRead(result);
        notificationRepository.save(notification.get());
        return result;
    }

    public Notification createNotification(Notification notification, long receiverId) {
        Optional<User> receiver = userRepository.findById(receiverId);
        if (receiver.isPresent()) {
            notification.setUser(receiver.get());
            notification = notificationRepository.save(notification);
            return notification;
        }

        return null;
    }
}
