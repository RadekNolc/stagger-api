package cz.radeknolc.stagger.service;

import cz.radeknolc.stagger.model.University;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.UserUniversity;
import cz.radeknolc.stagger.model.request.CreateUniversityRequest;
import cz.radeknolc.stagger.repository.UniversityRepository;
import cz.radeknolc.stagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserRepository userRepository;

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public List<University> getUserUniversities(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(value -> value.getUniversities().stream().map(UserUniversity::getUniversity).collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    public University createUniversity(CreateUniversityRequest createUniversityRequest) {
        University newUniversity = new University();
        newUniversity.setAbbreviation(createUniversityRequest.getAbbreviation());
        newUniversity.setStagUrlAddress(createUniversityRequest.getStagUrlAddress());
        newUniversity = universityRepository.save(newUniversity);

        if (newUniversity.getId() > 0) {
            return newUniversity;
        }

        return null;
    }

    public boolean assignUser(long universityId, long userId) {
        return dismissAssignUser(universityId, userId, true);
    }

    public boolean dismissUser(long universityId, long userId) {
        return dismissAssignUser(universityId, userId, false);
    }

    private boolean dismissAssignUser(long universityId, long userId, boolean assign) {
        Optional<University> university = universityRepository.findById(universityId);
        Optional<User> user = userRepository.findById(userId);

        // University or user does not exist
        if (university.isEmpty() || user.isEmpty()) {
            return false;
        }

        UserUniversity userUniversity = new UserUniversity(user.get(), university.get());

        if (assign) {
            // Checking if user is already assigned
            if (user.get().getUniversities().contains(userUniversity)) {
                return false;
            }

            user.get().getUniversities().add(userUniversity);
        } else {
            if (!user.get().getUniversities().contains(userUniversity)) {
                return false;
            }

            user.get().getUniversities().remove(userUniversity);
        }

        userRepository.save(user.get());
        return true;
    }
}
