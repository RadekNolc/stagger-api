package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.payload.ServerResponse;
import cz.radeknolc.stagger.model.request.ReadNotificationRequest;
import cz.radeknolc.stagger.service.NotificationService;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/read")
    public ResponseEntity<Void> readNotification(@Valid @RequestBody ReadNotificationRequest readNotificationRequest) {
        boolean readNotification = notificationService.readNotification(readNotificationRequest.getNotificationId());
        if (readNotification) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ServerResponse<List<Notification>>> userNotifications() {
        List<Notification> notifications = notificationService.getUserNotifications(AuthenticationUtils.getLoggedUser().getId());
        return ResponseEntity.ok(new ServerResponse<>(notifications));
    }
}
