package com.app.todolist.api.members.service;

import com.app.todolist.api.members.service.dto.MemberCreateInfo;
import com.app.todolist.api.members.service.dto.MemberLoginInfo;
import com.app.todolist.config.auth.AuthProperties;
import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final AuthProperties authProperties;

    private final MemberRepository memberRepository;

    private final RedisTemplate<String, MemberSession> redisTemplate;

    @Transactional
    public Member createMember(MemberCreateInfo memberCreateInfo) {
        validateMember(memberCreateInfo.getEmail());
        String hashedPassword = BCrypt.hashpw(memberCreateInfo.getPassword(), BCrypt.gensalt());
        return memberRepository.save(Member.create(memberCreateInfo.getEmail(), hashedPassword));
    }

    public Cookie login(MemberLoginInfo memberLoginInfo) {
        Member member = memberRepository.findByEmail(memberLoginInfo.getEmail()).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.AUTHENTICATION_FAILED));

        if (!BCrypt.checkpw(memberLoginInfo.getPassword(), member.getPassword())) {
            throw new TodoApplicationException(ErrorCode.AUTHENTICATION_FAILED);
        }

        String sessionId = new StandardSessionIdGenerator().generateSessionId();
        String sessionKey = authProperties.getSessionPrefix() + sessionId;
        redisTemplate.opsForValue().set(sessionKey, new MemberSession(member.getId()), 30L, TimeUnit.MINUTES);

        Cookie cookie = new Cookie(authProperties.getCookieName(), sessionId);
        cookie.setMaxAge(1800);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // 추후 적용 예정
        // myCookie.setSecure(true); https 에서만 요청 허용
        // myCookie.setDomain("exemple.com"); 요청 도메인 동적 처리
        return cookie;
    }

    public Cookie logout(String sessionId) {
        String sessionKey = authProperties.getSessionPrefix() + sessionId;
        redisTemplate.delete(sessionKey);

        Cookie cookie = new Cookie(authProperties.getCookieName(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    private void validateMember(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new TodoApplicationException(ErrorCode.DUPLICATED_MEMBER_ID);
        }
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
