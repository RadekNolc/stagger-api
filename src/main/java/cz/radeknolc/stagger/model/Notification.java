package cz.radeknolc.stagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "notification")
public class Notification extends AuditedEntity {

    @Enumerated(value = EnumType.STRING)
    private NotificationCategory category;
    private String title;
    private String description;
    private String icon;
    @Enumerated(value = EnumType.STRING)
    private NotificationState state;
    private boolean isRead;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;

    public Notification(NotificationState state, String title, String description, String icon) {
        this(NotificationCategory.ALERT, state, title, description, icon);
    }

    public Notification(NotificationCategory category, NotificationState state, String title, String description, String icon) {
        this(category, title, description, icon, state, false, null);
    }
}
