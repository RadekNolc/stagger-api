package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.payload.ServerResponse;
import cz.radeknolc.stagger.model.payload.TokenResponse;
import cz.radeknolc.stagger.model.payload.UserDetailsResponse;
import cz.radeknolc.stagger.model.request.LoginRequest;
import cz.radeknolc.stagger.model.request.VerifyTokenRequest;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cz.radeknolc.stagger.model.payload.ServerResponseMessage.AUTH_TOKEN_INVALID;

@RestController
@RequestMapping(path = "authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = authenticationUtils.generateToken(authentication);

        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<ServerResponse<UserDetailsResponse>> verifyToken(@RequestBody VerifyTokenRequest verifyTokenRequest) {
        if (authenticationUtils.validateToken(verifyTokenRequest.getToken())) {
            User user = authenticationUtils.getUserFromToken(verifyTokenRequest.getToken());
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse(user);
            if (user != null) {
                return ResponseEntity.ok(new ServerResponse<>(userDetailsResponse));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_TOKEN_INVALID));
    }
}
