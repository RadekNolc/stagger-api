package cz.radeknolc.stagger.model.request;

import cz.radeknolc.stagger.annotation.Exists;
import cz.radeknolc.stagger.model.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAssignUniversityRequest {

    @Exists(entityClass = University.class, targetColumn = "id", message = "NOT_EXISTS")
    private long universityId;
}
