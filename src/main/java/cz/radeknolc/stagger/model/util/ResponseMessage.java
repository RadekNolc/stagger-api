package cz.radeknolc.stagger.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessage<T> {

    private T content;
    private ResponseMessageLanguage language;
    private static final ResponseMessageLanguage defaultLanguage = ResponseMessageLanguage.EN;

    public ResponseMessage(T content) {
        this(content, null);
    }
}
