package org.zcorp.java2;

import org.slf4j.Logger;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.zcorp.java2.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.zcorp.java2.web.json.JsonUtil.writeValue;

public class TestUtil {

    private static final Logger log = getLogger(TestUtil.class);

    private static String getContent(ResultActions action) throws UnsupportedEncodingException {
        return action.andReturn().getResponse().getContentAsString();
    }

    public static ResultActions print(ResultActions action) throws UnsupportedEncodingException {
        log.info("response =\n" + getContent(action));
        return action;
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action), clazz);
    }

    private static <T> ResultMatcher contentJson(T expected) {
        return content().json(writeValue(expected));
    }

    public static <T> ResultMatcher contentJsonArray(T... expected) {
        return contentJson(expected);
    }

    public static <T> ResultMatcher contentJsonCollection(Collection<T> expected) {
        return contentJson(expected);
    }

}