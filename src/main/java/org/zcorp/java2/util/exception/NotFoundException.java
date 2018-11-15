package org.zcorp.java2.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//https://stackoverflow.com/questions/22358281/400-vs-422-response-to-post-that-references-an-unknown-entity/22358422#22358422
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "No data found") // 422
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}