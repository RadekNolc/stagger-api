package cz.radeknolc.stagger.model.payload;

import cz.radeknolc.stagger.model.Subject;
import cz.radeknolc.stagger.model.University;
import cz.radeknolc.stagger.model.User;
import cz.radeknolc.stagger.model.UserUniversity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse {

    private long id;
    private String abbreviation;
    private String stagUrlAddress;
    private Set<Subject> subjects;
    private Set<User> users;

    public UniversityResponse(University university) {
        if (university != null) {
            id = university.getId();
            abbreviation = university.getAbbreviation();
            stagUrlAddress = university.getStagUrlAddress();
            subjects = university.getSubjects();
            users = university.getUsers().stream().map(UserUniversity::getUser).collect(Collectors.toSet());
        }
    }
}
