package org.zcorp.java2.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.zcorp.java2.AuthorizedUser;
import org.zcorp.java2.util.ValidationUtil;
import org.zcorp.java2.web.validator.MessageUtil;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;
import static org.zcorp.java2.util.exception.ErrorType.APP_ERROR;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        log.error("Exception at request " + request.getRequestURL(), rootCause);

        ModelAndView modelAndView = new ModelAndView("exception/exception");
        modelAndView.addObject("typeMessage", messageUtil.getMessage(APP_ERROR.getErrorCode()));
        modelAndView.addObject("exception", rootCause);
        modelAndView.addObject("message", ValidationUtil.getMessage(rootCause));

        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            modelAndView.addObject("userTo", authorizedUser.getUserTo());
        }

        return modelAndView;
    }
}
