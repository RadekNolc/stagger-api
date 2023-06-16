package cz.radeknolc.stagger.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
