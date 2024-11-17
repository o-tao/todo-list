package com.app.todolist.web.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class CookieUtil {

    public static String getSessionIdFromCookies(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() != null) {
            return Arrays.stream(httpServletRequest.getCookies())
                    .filter(cookie -> "SESSION".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
