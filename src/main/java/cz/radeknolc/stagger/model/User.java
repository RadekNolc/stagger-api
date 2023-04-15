package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.radeknolc.stagger.model.util.ResponseMessageLanguage;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private ResponseMessageLanguage language;
    private Boolean isActive;

    public User(Long id, String username, String password, String emailAddress, String phoneNumber, ResponseMessageLanguage language, Boolean isActive) {
        super(id);
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User target)) return false; // Kontrola, zda neporovnáváme uživatele s lachtanem
        if (!getUsername().equals(target.getUsername())) return false;
        return getEmailAddress().equals(target.getEmailAddress());
    }
}
