package com.app.todolist.api.session.service;

import com.app.todolist.config.redis.dto.MemberSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final RedisTemplate<String, MemberSession> redisTemplate;

    private static final long SESSION_TIMEOUT = 30;
    private static final String SESSION_PREFIX = "TODO_SESSION:";

    public String createSession(Long memberId) {
        String sessionId = new StandardSessionIdGenerator().generateSessionId();
        String sessionKey = SESSION_PREFIX + sessionId;
        redisTemplate.opsForValue().set(sessionKey, new MemberSession(memberId), SESSION_TIMEOUT, TimeUnit.MINUTES);
        return sessionId;
    }

    public void deleteSession(String sessionId) {
        String sessionKey = SESSION_PREFIX + sessionId;
        redisTemplate.delete(sessionKey);
    }

    public MemberSession findSession(String sessionId) {
        MemberSession memberSession = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        if (memberSession != null) {
            redisTemplate.expire(SESSION_PREFIX + sessionId, SESSION_TIMEOUT, TimeUnit.MINUTES);
        }
        return memberSession;
    }
}
