package com.app.todolist.config.auth.checkAuth;

import com.app.todolist.api.session.service.SessionService;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import com.app.todolist.web.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            CheckAuth checkAuth = handlerMethod.getMethodAnnotation(CheckAuth.class);
            if (checkAuth != null) {
                String sessionId = CookieUtil.getSessionIdFromCookies(httpServletRequest);

                if (sessionId == null || sessionService.findSession(sessionId) == null) {
                    throw new TodoApplicationException(ErrorCode.LOGIN_FORBIDDEN);
                }
            }
        }
        return true;
    }
}
