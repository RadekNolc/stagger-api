package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.payload.ExtendedTokenResponse;
import cz.radeknolc.stagger.model.payload.ServerResponse;
import cz.radeknolc.stagger.model.payload.TokenResponse;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new ExtendedTokenResponse(token, user.getId(), user.getUsername(), user.getEmailAddress(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<ServerResponse<User>> verifyToken(@RequestBody VerifyTokenRequest verifyTokenRequest) {
        if (authenticationUtils.validateToken(verifyTokenRequest.getToken())) {
            User user = authenticationUtils.getUserFromToken(verifyTokenRequest.getToken());
            if (user != null) {
                return ResponseEntity.ok(new ServerResponse<>(user));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_TOKEN_INVALID));
    }
}
