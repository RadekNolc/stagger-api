package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Transient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Transient
    enum NotificationType {
        PRIMARY,
        SECONDARY,
        SUCCESS,
        DANGER,
    }

    @Transient
    enum NotificationIcon {
        BELL,
        ACTIVITY,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;
    @Enumerated(value = EnumType.STRING)
    private NotificationIcon icon;
    private String message;
    private Boolean isRead;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;
}
