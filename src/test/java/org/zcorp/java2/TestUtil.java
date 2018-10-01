package org.zcorp.java2;

import org.slf4j.Logger;
import org.springframework.test.web.servlet.ResultActions;
import org.zcorp.java2.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;

import static org.slf4j.LoggerFactory.getLogger;

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

}