package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.radeknolc.stagger.model.util.TextLanguage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    private String username;
    @JsonIgnore
    private String password;
    private String emailAddress;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private TextLanguage language;
    private Boolean isActive;
    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "roles_users_map",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User target)) return false; // Kontrola, zda neporovnáváme uživatele s lachtanem
        if (!getUsername().equals(target.getUsername())) return false;
        return getEmailAddress().equals(target.getEmailAddress());
    }
}
