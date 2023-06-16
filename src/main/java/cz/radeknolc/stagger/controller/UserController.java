package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUserRequest;
import cz.radeknolc.stagger.model.response.CreateUserResponse;
import cz.radeknolc.stagger.model.response.ServerResponse;
import cz.radeknolc.stagger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cz.radeknolc.stagger.model.response.ServerResponseMessage.USER_REGISTRATION_SUCCESS;

@RestController
@RequestMapping(path = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<ServerResponse<CreateUserResponse>> registerUser(@Valid @RequestBody CreateUserRequest request) {
        User createdUser = userService.registerUser(request);
        CreateUserResponse response = new CreateUserResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServerResponse<>(USER_REGISTRATION_SUCCESS, response));
    }
}
