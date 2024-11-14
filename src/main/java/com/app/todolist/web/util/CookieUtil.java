package com.app.todolist.web.util;

import com.app.todolist.config.auth.AuthProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieUtil {

    private static AuthProperties authProperties;

    public static void setAuthProperties(AuthProperties authProperties) {
        CookieUtil.authProperties = authProperties;
    }

    public static String getSessionIdFromCookies(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() != null) {
            return Arrays.stream(httpServletRequest.getCookies())
                    .filter(cookie -> authProperties.getCookieName().equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
