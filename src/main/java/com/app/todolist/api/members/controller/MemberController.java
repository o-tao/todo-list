package com.app.todolist.api.members.controller;

import com.app.todolist.api.members.controller.dto.request.MemberLoginRequest;
import com.app.todolist.api.members.controller.dto.request.MemberRequest;
import com.app.todolist.api.members.controller.dto.response.MemberResponse;
import com.app.todolist.api.members.service.MemberService;
import com.app.todolist.domain.members.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public MemberResponse createMember(@RequestBody @Valid MemberRequest memberRequest) {
        Member member = memberService.createMember(memberRequest.toCreate());
        return MemberResponse.of(member);
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid MemberLoginRequest memberLoginRequest,
                      HttpServletResponse httpServletResponse) {
        Cookie cookie = memberService.login(memberLoginRequest.toLogin());
        httpServletResponse.addCookie(cookie);
    }
}
