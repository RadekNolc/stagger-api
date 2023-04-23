package cz.radeknolc.stagger.model.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.radeknolc.stagger.model.Role;
import cz.radeknolc.stagger.model.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "roles_users_map")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long mapId;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;
    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
}
