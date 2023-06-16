package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.ReadNotificationRequest;
import cz.radeknolc.stagger.model.response.ReadNotificationResponse;
import cz.radeknolc.stagger.model.response.ServerResponse;
import cz.radeknolc.stagger.model.response.NotificationResponse;
import cz.radeknolc.stagger.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static cz.radeknolc.stagger.model.response.ServerResponseMessage.*;

@RestController
@RequestMapping(path = "notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/read")
    public ResponseEntity<ServerResponse<ReadNotificationResponse>> readNotification(@AuthenticationPrincipal User user, @Valid @RequestBody ReadNotificationRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_USER_VERIFY_ERROR));
        }

        boolean success = notificationService.readNotification(request.getNotificationId(), user.getId());
        ReadNotificationResponse response = new ReadNotificationResponse(success);
        if (success) {
            return ResponseEntity.ok(new ServerResponse<>(NOTIFICATION_READ, response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(NOTIFICATION_NOT_READ));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ServerResponse<List<NotificationResponse>>> userNotifications(@AuthenticationPrincipal User user, @RequestParam String userId) {
        if (user != null && user.getId() == Integer.parseInt(userId)) {
            List<NotificationResponse> response = new ArrayList<>(NotificationResponse.parseList(notificationService.getUserNotifications(user.getId())));
            return ResponseEntity.ok(new ServerResponse<>(GENERAL_OK, response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_USER_VERIFY_ERROR));
    }
}
