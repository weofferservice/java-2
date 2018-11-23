package org.zcorp.java2.util.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.zcorp.java2.util.exception.ErrorType.APP_ERROR;

public class ApplicationException extends Java2Exception {

    private final HttpStatus httpStatus;
    private final ErrorType type;
    private final String msgCode;
    private final String[] msgArgs;

    public ApplicationException(HttpStatus httpStatus, String msgCode) {
        this(httpStatus, APP_ERROR, msgCode);
    }

    public ApplicationException(HttpStatus httpStatus, ErrorType type, String msgCode, String... msgArgs) {
        super(String.format("type=%s, msgCode=%s, args=%s", type, msgCode, Arrays.toString(msgArgs)));
        this.httpStatus = httpStatus;
        this.type = type;
        this.msgCode = msgCode;
        this.msgArgs = msgArgs;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public String[] getMsgArgs() {
        return msgArgs;
    }

}
