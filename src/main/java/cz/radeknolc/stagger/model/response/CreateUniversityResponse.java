package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUniversityResponse {

    private String abbreviation;
    private String stagUrlAddress;

    public CreateUniversityResponse(University university) {
        abbreviation = university.getAbbreviation();
        stagUrlAddress = university.getStagUrlAddress();
    }
}
