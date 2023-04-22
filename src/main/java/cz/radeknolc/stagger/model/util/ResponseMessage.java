package cz.radeknolc.stagger.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessage<T> {

    private T content;
    private TextLanguage language;

    public ResponseMessage(T content) {
        this(content, null);
    }
}
