package org.zcorp.java2.web;

import static org.zcorp.java2.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static org.zcorp.java2.util.UsersUtil.USER_ID;

public class SecurityUtil {

    private static int authUserId = USER_ID;

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