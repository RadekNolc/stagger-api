package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@Table(name = "universities_users_map")
@NoArgsConstructor
@AllArgsConstructor
public class UserUniversity extends BaseEntity {

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;
    @ManyToOne(targetEntity = University.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private University university;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserUniversity target)) return false;
        return Objects.equals(user, target.user) && Objects.equals(university, target.university);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, university);
    }
}
