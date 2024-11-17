package com.app.todolist.config.auth.checkAuth;

import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import com.app.todolist.web.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, MemberSession> redisTemplate;

    private static final String SESSION_KEY = "TODO_SESSION:";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            CheckAuth checkAuth = handlerMethod.getMethodAnnotation(CheckAuth.class);
            if (checkAuth != null) {
                String sessionId = CookieUtil.getSessionIdFromCookies(httpServletRequest);

                if (sessionId == null || redisTemplate.opsForValue().get(SESSION_KEY + sessionId) == null) {
                    throw new TodoApplicationException(ErrorCode.LOGIN_FORBIDDEN);
                }
            }
        }
        return true;
    }
}
