package org.zcorp.java2.web;

import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.zcorp.java2.util.ValidationUtil;
import org.zcorp.java2.util.exception.*;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionInfoHandler {
    private static final Logger log = getLogger(ExceptionInfoHandler.class);

    //https://stackoverflow.com/questions/22358281/400-vs-422-response-to-post-that-references-an-unknown-entity/22358422#22358422
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest request, NotFoundException e) {
        return logAndGetErrorInfo(request, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest request, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(request, e, true, DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    @ExceptionHandler({ValidationException.class, IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest request, Exception e) {
        return logAndGetErrorInfo(request, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest request, Exception e) {
        return logAndGetErrorInfo(request, e, true, APP_ERROR);
    }

    //https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest request, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + request.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request {}: {}", errorType, request.getRequestURL(), rootCause.toString());
        }
        return new ErrorInfo(request.getRequestURL(), errorType, ValidationUtil.getMessage(rootCause));
    }
}