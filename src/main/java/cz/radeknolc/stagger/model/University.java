package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.radeknolc.stagger.model.map.UserUniversity;
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
public class University extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(targetEntity = Subject.class, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "university")
    private Set<Subject> subjects;
    @OneToMany(mappedBy = "university")
    @JsonIgnore
    private Set<UserUniversity> users;
}
