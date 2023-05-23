package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.UserDetailsImpl;
import cz.radeknolc.stagger.model.payload.ServerResponse;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.service.NotificationService;
import cz.radeknolc.stagger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User newUser = userService.registerUser(createUserRequest);
        return ResponseEntity.ok().body(new ServerResponse<>("REGISTRATION_SUCCESS"));
    }

    @GetMapping(value = "/notifications")
    public ResponseEntity<?> userNotifications() {
        List<Notification> notifications = new ArrayList<>();
        if (UserDetailsImpl.getLoggedUser() != null) {
            notifications = notificationService.getUserNotifications(UserDetailsImpl.getLoggedUser().getId());
        }

        return ResponseEntity.ok().body(notifications);
    }
}
