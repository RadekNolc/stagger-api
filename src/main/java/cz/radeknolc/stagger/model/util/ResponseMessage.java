package cz.radeknolc.stagger.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessage {

    private ResponseMessageType status;
    private String message;

    public ResponseMessage(String message) {
        status = ResponseMessageType.OK;
        this.message = message;
    }
}
