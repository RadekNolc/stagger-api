package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "universities_users_map")
public class UserUniversity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long mapId;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;
    @ManyToOne(targetEntity = University.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private University university;
}
