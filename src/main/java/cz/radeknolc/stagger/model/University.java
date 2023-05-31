package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "university")
public class University extends AuditedEntity {

    private String name;
    @OneToMany(targetEntity = Subject.class, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "university")
    @JsonIgnore
    private Set<Subject> subjects;
    @OneToMany(mappedBy = "university")
    @JsonIgnore
    private Set<UserUniversity> users;
}
