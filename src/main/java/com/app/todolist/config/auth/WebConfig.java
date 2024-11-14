package com.app.todolist.config.auth;

import com.app.todolist.config.auth.checkAuth.AuthInterceptor;
import com.app.todolist.config.auth.loginMember.LoginMemberArgumentResolver;
import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.web.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final RedisTemplate<String, MemberSession> redisTemplate;
    private final AuthProperties authProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        CookieUtil.setAuthProperties(authProperties);
        registry.addInterceptor(new AuthInterceptor(authProperties, redisTemplate))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/members/login", "/api/members");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}
