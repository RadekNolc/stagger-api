package cz.radeknolc.stagger.model;

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
public class Role extends BaseEntity implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 3929235466956026347L;

    @Enumerated(EnumType.STRING)
    private RoleName name;
    @OneToMany(mappedBy = "role")
    private Set<UserRole> users;

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
