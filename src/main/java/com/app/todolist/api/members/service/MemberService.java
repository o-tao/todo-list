package com.app.todolist.api.members.service;

import com.app.todolist.api.members.service.dto.MemberLoginInfo;
import com.app.todolist.config.redis.dto.MemberSession;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final RedisTemplate<String, MemberSession> redisTemplate;

    @Transactional
    public Member createMember(Member member) {
        validateMember(member.getEmail());
        return memberRepository.save(member);
    }

    public Cookie login(MemberLoginInfo memberLoginInfo) {
        Member member = memberRepository.findByEmailAndPassword(
                memberLoginInfo.getEmail(), memberLoginInfo.getPassword()).orElseThrow(()
                -> new TodoApplicationException(ErrorCode.AUTHENTICATION_FAILED));

        String sessionId = new StandardSessionIdGenerator().generateSessionId();
        String sessionKey = "TODO_SESSION:" + sessionId;
        redisTemplate.opsForValue().set(sessionKey, new MemberSession(member.getId()), 30L, TimeUnit.MINUTES);

        Cookie cookie = new Cookie("SESSION", sessionId);
        cookie.setMaxAge(1800);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // 추후 적용 예정
        // myCookie.setSecure(true); https 에서만 요청 허용
        // myCookie.setDomain("exemple.com"); 요청 도메인 동적 처리
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
