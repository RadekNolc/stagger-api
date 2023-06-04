package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.University;
import cz.radeknolc.stagger.model.payload.ServerResponse;
import cz.radeknolc.stagger.model.request.UserAssignUniversityRequest;
import cz.radeknolc.stagger.model.request.CreateUniversityRequest;
import cz.radeknolc.stagger.model.request.UserDismissUniversity;
import cz.radeknolc.stagger.service.UniversityService;
import cz.radeknolc.stagger.util.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cz.radeknolc.stagger.model.payload.ServerResponseMessage.*;

@RestController
@RequestMapping(path = "university")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping(value = "/all")
    public ResponseEntity<ServerResponse<List<University>>> allUniversities() {
        return ResponseEntity.ok(new ServerResponse<>(universityService.getAllUniversities()));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ServerResponse<University>> createUniversity(@Valid @RequestBody CreateUniversityRequest createUniversityRequest) {
        University newUniversity = universityService.createUniversity(createUniversityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServerResponse<>(UNIVERSITY_CREATED, newUniversity));
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ServerResponse<List<University>>> userUniversities() {
        List<University> userUniversities = universityService.getUserUniversities(AuthenticationUtils.getLoggedUser().getId());
        return ResponseEntity.ok(new ServerResponse<>(userUniversities));
    }

    @PostMapping(value = "/assign")
    public ResponseEntity<ServerResponse<Void>> userAssignUniversity(@Valid @RequestBody UserAssignUniversityRequest userAssignUniversityRequest) {
        boolean userAssigned = universityService.assignUser(userAssignUniversityRequest.getUniversityId(), AuthenticationUtils.getLoggedUser().getId());
        if (userAssigned) {
            return ResponseEntity.ok(new ServerResponse<>(UNIVERSITY_ASSIGNED));
        }

        return ResponseEntity.badRequest().body(new ServerResponse<>(UNIVERSITY_NOT_ASSIGNED));
    }

    @PostMapping(value = "/dismiss")
    public ResponseEntity<ServerResponse<Void>> userDismissUniversity(@Valid @RequestBody UserDismissUniversity userDismissUniversity) {
        boolean userDismissed = universityService.dismissUser(userDismissUniversity.getUniversityId(), AuthenticationUtils.getLoggedUser().getId());
        if (userDismissed) {
            return ResponseEntity.ok(new ServerResponse<>(UNIVERSITY_DISMISSED));
        }

        return ResponseEntity.badRequest().body(new ServerResponse<>(UNIVERSITY_NOT_DISMISSED));
    }
}
