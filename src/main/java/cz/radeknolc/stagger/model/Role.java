package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 3929235466956026347L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName name;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<UserRole> users;

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
