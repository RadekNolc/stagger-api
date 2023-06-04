package cz.radeknolc.stagger.model.request;

import cz.radeknolc.stagger.annotation.Unique;
import cz.radeknolc.stagger.model.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUniversityRequest {

    @Unique(entityClass = University.class, targetColumn = "name", message = "UNIQUE")
    private String name;
}
