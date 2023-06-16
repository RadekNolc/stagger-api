package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.response.ServerResponse;
import cz.radeknolc.stagger.model.response.TokenResponse;
import cz.radeknolc.stagger.model.response.VerifyTokenResponse;
import cz.radeknolc.stagger.model.request.TokenRequest;
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

import static cz.radeknolc.stagger.model.response.ServerResponseMessage.*;

@RestController
@RequestMapping(path = "authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<ServerResponse<TokenResponse>> authenticate(@Valid @RequestBody TokenRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenResponse response = new TokenResponse(authenticationUtils.generateToken(authentication));
        return ResponseEntity.ok(new ServerResponse<>(GENERAL_SUCCESS, response));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<ServerResponse<VerifyTokenResponse>> verifyToken(@Valid @RequestBody VerifyTokenRequest request) {
        if (authenticationUtils.validateToken(request.getToken())) {
            User user = authenticationUtils.getUserFromToken(request.getToken());
            VerifyTokenResponse response = new VerifyTokenResponse(user);
            return ResponseEntity.ok(new ServerResponse<>(GENERAL_SUCCESS, response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_TOKEN_INVALID));
    }
}
