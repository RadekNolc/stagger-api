package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.model.response.CreateUserResponse;
import cz.radeknolc.stagger.model.response.ServerResponse;
import cz.radeknolc.stagger.model.response.UserProfileResponse;
import cz.radeknolc.stagger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static cz.radeknolc.stagger.model.response.ServerResponseMessage.*;

@RestController
@RequestMapping(path = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/profile")
    public ResponseEntity<ServerResponse<UserProfileResponse>> userProfile(@AuthenticationPrincipal User user, @RequestParam String userId) {
        if (user != null && user.getId() == Integer.parseInt(userId)) {
            UserProfileResponse response = new UserProfileResponse(user);
            return ResponseEntity.ok(new ServerResponse<>(GENERAL_OK, response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_USER_VERIFY_ERROR));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ServerResponse<CreateUserResponse>> registerUser(@Valid @RequestBody CreateUserRequest request) {
        User createdUser = userService.registerUser(request);
        CreateUserResponse response = new CreateUserResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServerResponse<>(USER_REGISTRATION_SUCCESS, response));
    }
}
