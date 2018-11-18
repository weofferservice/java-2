package org.zcorp.java2;

import org.springframework.test.web.servlet.ResultMatcher;
import org.zcorp.java2.util.exception.ErrorInfo;
import org.zcorp.java2.util.exception.ErrorType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.zcorp.java2.web.json.JsonUtil.writeIgnoreProps;

public class ErrorInfoTestData {
    private static final ErrorInfo VALIDATION_ERROR_INFO = new ErrorInfo(null, ErrorType.VALIDATION_ERROR, null);

    public static ResultMatcher contentValidationErrorInfoJson() {
        return content().json(writeIgnoreProps(VALIDATION_ERROR_INFO, "url", "details"));
    }
}
