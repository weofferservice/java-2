package org.zcorp.java2.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zcorp.java2.AuthorizedUser;

import java.util.Objects;

public class SecurityUtil {

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        Objects.requireNonNull(user, "Not authorized user found");
        return user;
    }

    public static int authUserId() {
        return get().getId();
    }

    public static int authUserCaloriesPerDay() {
        return get().getUserTo().getCaloriesPerDay();
    }

}