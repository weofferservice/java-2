package org.zcorp.java2.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.zcorp.java2.util.ValidationUtil;
import org.zcorp.java2.util.exception.ApplicationException;
import org.zcorp.java2.util.exception.ErrorType;
import org.zcorp.java2.web.validator.MessageUtil;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.exception.ErrorType.APP_ERROR;
import static org.zcorp.java2.util.exception.ErrorType.WRONG_REQUEST;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView applicationErrorHandler(HttpServletRequest request, ApplicationException appEx) {
        return logAndGetExceptionView(request, appEx, false, appEx.getType(), messageUtil.getMessage(appEx));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest request, NoHandlerFoundException e) {
        return logAndGetExceptionView(request, e, false, WRONG_REQUEST, null);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        return logAndGetExceptionView(request, e, true, APP_ERROR, null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest request, Exception e, boolean logException, ErrorType errorType, String msg) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + request.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request {}: {}", errorType, request.getRequestURL(), rootCause.toString());
        }

        ModelAndView modelAndView = new ModelAndView("exception/exception");
        modelAndView.addObject("typeMessage", messageUtil.getMessage(errorType.getErrorCode()));
        modelAndView.addObject("exception", rootCause);
        modelAndView.addObject("message", msg != null ? msg : ValidationUtil.getMessage(rootCause));

        return modelAndView;
    }
}
