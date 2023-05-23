package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.request.ReadNotificationRequest;
import cz.radeknolc.stagger.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/read")
    public ResponseEntity<?> readNotification(@Valid @RequestBody ReadNotificationRequest readNotificationRequest) {
        return ResponseEntity.status(notificationService.readNotification(readNotificationRequest.getNotificationId()) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).build();
    }
}
