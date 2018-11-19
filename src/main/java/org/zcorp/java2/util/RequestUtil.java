package org.zcorp.java2.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtil {

    private RequestUtil() {
    }

    public static Integer getIdFromRequest(String restUrl) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            Pattern pattern = Pattern.compile(restUrl + "/(\\d+)$");
            Matcher matcher = pattern.matcher(request.getRequestURI());
            if (matcher.find()) {
                return Integer.valueOf(matcher.group(1));
            }
        }
        return null;
    }

}
