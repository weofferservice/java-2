package org.zcorp.java2.util.exception;

import org.springframework.lang.NonNull;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.zcorp.java2.util.exception.ErrorType.DATA_NOT_FOUND;

public class NotFoundException extends ApplicationException {
    public static final String EXCEPTION_NOT_FOUND = "exception.entityNotFound";

    public NotFoundException(@NonNull String msgArg) {
        super(UNPROCESSABLE_ENTITY, // 422 // https://stackoverflow.com/questions/22358281/400-vs-422-response-to-post-that-references-an-unknown-entity/22358422#22358422
                DATA_NOT_FOUND,
                EXCEPTION_NOT_FOUND,
                msgArg);
    }
}