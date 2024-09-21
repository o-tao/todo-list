package com.app.todolist.api.members;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.MemberRepository;
import com.app.todolist.exception.ApplicationException;
import com.app.todolist.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(Member member) {
        validateMember(member.getEmail());
        return memberRepository.save(member);
    }

    public void validateMember(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.DUPLICATED_MEMBER_ID);
        }
    }

}
