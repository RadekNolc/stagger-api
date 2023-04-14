package cz.radeknolc.stagger.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessage {

    private String message;
    private ResponseMessageLanguage language;
    private static final ResponseMessageLanguage defaultLanguage = ResponseMessageLanguage.EN;

    public ResponseMessage(String message) {
        this.message = message;
        this.language = defaultLanguage;
    }
}
