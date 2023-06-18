package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse {

    private long id;
    private String abbreviation;
    private String stagUrlAddress;

    public UniversityResponse(University university) {
        id = university.getId();
        abbreviation = university.getAbbreviation();
        stagUrlAddress = university.getStagUrlAddress();
    }

    public static List<UniversityResponse> parseList(List<University> universities) {
        List<UniversityResponse> result = new ArrayList<>();
        for (University university : universities) {
            result.add(new UniversityResponse(university));
        }

        return result;
    }
}
