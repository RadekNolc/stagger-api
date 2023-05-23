package cz.radeknolc.stagger.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadNotificationRequest {

    @NotNull(message = "NOT_NULL")
    private Long notificationId;
}
