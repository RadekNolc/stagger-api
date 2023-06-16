package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.University;
import cz.radeknolc.stagger.model.UserUniversity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse {

    private long id;
    private String abbreviation;
    private String stagUrlAddress;
    private List<SubjectResponse> subjects;
    private List<UserResponse> users;

    public UniversityResponse(University university) {
        id = university.getId();
        abbreviation = university.getAbbreviation();
        stagUrlAddress = university.getStagUrlAddress();
        subjects = SubjectResponse.parseList(university.getSubjects().stream().toList());
        users = UserResponse.parseList(university.getUsers().stream().map(UserUniversity::getUser).collect(Collectors.toList()));
    }

    public static List<UniversityResponse> parseList(List<University> universities) {
        List<UniversityResponse> result = new ArrayList<>();
        for (University university : universities) {
            result.add(new UniversityResponse(university));
        }

        return result;
    }
}
