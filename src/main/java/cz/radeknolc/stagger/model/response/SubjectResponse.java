package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.Subject;
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
public class SubjectResponse {

    private long id;
    private String department;
    private String name;

    public SubjectResponse(Subject subject) {
        id = subject.getId();
        department = subject.getDepartment();
        name = subject.getName();
    }

    public static List<SubjectResponse> parseList(List<Subject> subjects) {
        List<SubjectResponse> result = new ArrayList<>();
        for (Subject subject : subjects) {
            result.add(new SubjectResponse(subject));
        }

        return result;
    }
}
