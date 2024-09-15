package com.app.todolist.api.members;

import com.app.todolist.domain.members.Member;
import com.app.todolist.domain.members.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(Member member) {
        return memberRepository.save(member);
    }

}
