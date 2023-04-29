package cz.radeknolc.stagger.model.payload;

import cz.radeknolc.stagger.model.TextLanguage;
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
