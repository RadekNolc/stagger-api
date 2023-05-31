package cz.radeknolc.stagger.model.request;

import cz.radeknolc.stagger.annotation.Exists;
import cz.radeknolc.stagger.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadNotificationRequest {

    @Exists(entityClass = Notification.class, targetColumn = "id", message = "NOT_EXISTS")
    private long notificationId;
}
