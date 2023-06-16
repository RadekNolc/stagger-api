package cz.radeknolc.stagger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subject")
public class Subject extends BaseEntity {

    private String department;
    private String name;
    @ManyToOne(targetEntity = University.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "university_id", referencedColumnName = "id", nullable = false)
    private University university;
}
