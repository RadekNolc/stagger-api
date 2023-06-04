package cz.radeknolc.stagger.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerResponse<T> {

    private ServerResponseMessage message;
    private T content;

    public ServerResponse(String message) {
        this(ServerResponseMessage.valueOf(message), null);
    }
    public ServerResponse(T content) { this(null, content); }
    public ServerResponse(ServerResponseMessage message) {
        this(message, null);
    }
}
