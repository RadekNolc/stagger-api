package cz.radeknolc.stagger.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerResponse<T> {

    private String message;
    private T content;

    public ServerResponse(String message) {
        this(message, null);
    }
}
