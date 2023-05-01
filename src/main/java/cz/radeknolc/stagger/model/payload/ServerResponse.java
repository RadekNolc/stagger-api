package cz.radeknolc.stagger.model.payload;

import cz.radeknolc.stagger.model.TextLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerResponse<T> {

    private String message;
    private T content;
    private TextLanguage language;

    public ServerResponse(T content) {
        this(null, content);
    }

    public ServerResponse(String message, T content) {
        this(message, content, null);
    }

    public ServerResponse(String message) {
        this(message, null, null);
    }
}
