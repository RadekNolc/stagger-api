package cz.radeknolc.stagger.model.response;

import cz.radeknolc.stagger.model.Notification;
import cz.radeknolc.stagger.model.NotificationCategory;
import cz.radeknolc.stagger.model.NotificationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private long id;
    private NotificationCategory category;
    private String title;
    private String description;
    private String icon;
    private NotificationState state;
    private boolean isRead;
    private LocalDateTime sentDateTime;

    public NotificationResponse(Notification notification) {
        id = notification.getId();
        category = notification.getCategory();
        title = notification.getTitle();
        description = notification.getTitle();
        icon = notification.getIcon();
        state = notification.getState();
        isRead = notification.isRead();
        sentDateTime = notification.getCreatedAt();
    }

    public static List<NotificationResponse> parseList(List<Notification> notifications) {
        List<NotificationResponse> result = new ArrayList<>();
        for (Notification notification : notifications) {
            result.add(new NotificationResponse(notification));
        }

        return result;
    }
}
