package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.University;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.request.CreateUniversityRequest;
import cz.radeknolc.stagger.model.request.UserAssignUniversityRequest;
import cz.radeknolc.stagger.model.request.UserDismissUniversityRequest;
import cz.radeknolc.stagger.model.response.*;
import cz.radeknolc.stagger.service.UniversityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static cz.radeknolc.stagger.model.response.ServerResponseMessage.*;

@RestController
@RequestMapping(path = "university")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping(value = "/all")
    public ResponseEntity<ServerResponse<List<UniversityResponse>>> allUniversities() {
        List<UniversityResponse> response = new ArrayList<>(UniversityResponse.parseList(universityService.getAllUniversities()));
        return ResponseEntity.ok(new ServerResponse<>(GENERAL_OK, response));
    }

    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ServerResponse<List<UniversityResponse>>> userUniversities(@AuthenticationPrincipal User user, @RequestParam String userId) {
        if (user != null && user.getId() == Integer.parseInt(userId)) {
            List<UniversityResponse> response = new ArrayList<>(UniversityResponse.parseList(universityService.getUserUniversities(user.getId())));
            return ResponseEntity.ok(new ServerResponse<>(GENERAL_OK, response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_USER_VERIFY_ERROR));
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServerResponse<CreateUniversityResponse>> createUniversity(@Valid @RequestBody CreateUniversityRequest request) {
        University createdUniversity = universityService.createUniversity(request);
        CreateUniversityResponse response = new CreateUniversityResponse(createdUniversity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServerResponse<>(UNIVERSITY_CREATED, response));
    }

    @PostMapping(value = "/assign")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ServerResponse<UserAssignUniversityResponse>> userAssignUniversity(@AuthenticationPrincipal User user, @Valid @RequestBody UserAssignUniversityRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_USER_VERIFY_ERROR));
        }

        boolean success = universityService.assignUser(request.getUniversityId(), user.getId());
        UserAssignUniversityResponse response = new UserAssignUniversityResponse(success);
        if (success) {
            return ResponseEntity.ok(new ServerResponse<>(UNIVERSITY_ASSIGNED, response));
        }

        return ResponseEntity.badRequest().body(new ServerResponse<>(UNIVERSITY_NOT_ASSIGNED, response));
    }

    @PostMapping(value = "/dismiss")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ServerResponse<UserDismissUniversityResponse>> userDismissUniversity(@AuthenticationPrincipal User user, @Valid @RequestBody UserDismissUniversityRequest request) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_USER_VERIFY_ERROR));
        }

        boolean success = universityService.dismissUser(request.getUniversityId(), user.getId());
        UserDismissUniversityResponse response = new UserDismissUniversityResponse(success);
        if (success) {
            return ResponseEntity.ok(new ServerResponse<>(UNIVERSITY_DISMISSED, response));
        }

        return ResponseEntity.badRequest().body(new ServerResponse<>(UNIVERSITY_NOT_DISMISSED, response));
    }
}
