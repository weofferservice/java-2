package org.zcorp.java2.web;

import static org.zcorp.java2.util.UserUtil.DEFAULT_CALORIES_PER_DAY;
import static org.zcorp.java2.model.AbstractBaseEntity.START_SEQ;

public class SecurityUtil {

    private static int authUserId = START_SEQ;

    public static int authUserId() {
        return authUserId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void setAuthUserId(int userId) {
        authUserId = userId;
    }

}