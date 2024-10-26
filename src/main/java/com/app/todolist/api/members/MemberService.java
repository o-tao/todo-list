package com.app.todolist.api.members;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.repository.MemberRepository;
import com.app.todolist.web.exception.ErrorCode;
import com.app.todolist.web.exception.TodoApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        validateMember(member.getEmail());
        return memberRepository.save(member);
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
