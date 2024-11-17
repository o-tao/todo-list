package com.app.todolist.api.members.service;

import com.app.todolist.api.members.service.dto.MemberCreateInfo;
import com.app.todolist.api.members.service.dto.MemberLoginInfo;
import com.app.todolist.api.session.service.SessionService;
import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import com.app.todolist.web.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final SessionService sessionService;
    private final MemberRepository memberRepository;

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

        String sessionId = sessionService.createSession(member.getId());
        return CookieUtil.createCookie(sessionId);
    }

    public Cookie logout(String sessionId) {
        sessionService.deleteSession(sessionId);
        return CookieUtil.deleteCookie(sessionId);
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
