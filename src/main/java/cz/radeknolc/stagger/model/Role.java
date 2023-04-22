package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.radeknolc.stagger.model.util.RoleName;
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
@Table(name = "role")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleName name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;
}
