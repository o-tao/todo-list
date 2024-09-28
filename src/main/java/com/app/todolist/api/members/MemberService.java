package com.app.todolist.api.members;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.MemberRepository;
import com.app.todolist.exception.ErrorCode;
import com.app.todolist.exception.TodoApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(Member member) {
        validateMember(member.getEmail());
        return memberRepository.save(member);
    }

    public void validateMember(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new TodoApplicationException(ErrorCode.DUPLICATED_MEMBER_ID);
        }
    }

}
