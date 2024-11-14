package com.app.todolist.config.auth.loginMember;

import com.app.todolist.config.auth.AuthProperties;
import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.web.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthProperties authProperties;

    private final HttpServletRequest httpServletRequest;
    private final RedisTemplate<String, MemberSession> redisTemplate;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public MemberSession resolveArgument(MethodParameter parameter,
                                         ModelAndViewContainer mavContainer,
                                         NativeWebRequest webRequest,
                                         WebDataBinderFactory binderFactory) {
        String sessionId = CookieUtil.getSessionIdFromCookies(httpServletRequest);
        if (sessionId != null) {
            return redisTemplate.opsForValue().get(authProperties.getSessionPrefix() + sessionId);
        }
        return null;
    }
}
