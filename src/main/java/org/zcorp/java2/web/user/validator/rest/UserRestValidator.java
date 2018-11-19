package org.zcorp.java2.web.user.validator.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zcorp.java2.model.User;
import org.zcorp.java2.web.user.validator.AbstractUserValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.zcorp.java2.web.user.AdminRestController.REST_URL;

@Component
public class UserRestValidator extends AbstractUserValidator<User> {
    private static final Pattern ID_PATTERN = Pattern.compile(REST_URL + "/(\\d+)");

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    protected Integer getId(User user) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            Matcher matcher = ID_PATTERN.matcher(request.getRequestURI());
            if (matcher.find()) {
                return Integer.valueOf(matcher.group(1));
            }
        }
        return null;
    }

    @Override
    protected String getEmail(User user) {
        return user.getEmail();
    }
}
