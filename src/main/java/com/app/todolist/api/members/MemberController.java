package com.app.todolist.api.members;

import com.app.todolist.api.members.dto.MemberRequest;
import com.app.todolist.api.members.dto.MemberResponse;
import com.app.todolist.domain.members.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public MemberResponse create(@RequestBody @Valid MemberRequest memberRequest) {
        Member member = memberService.save(memberRequest.toEntity());
        return MemberResponse.of(member);
    }
}
