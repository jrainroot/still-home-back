package jrainroot.still_home.controller;

import jrainroot.still_home.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ApiMemberController {
    private final MemberService memberService;

    @GetMapping("/test")
    public String memberTest() {
        return "멤버 테스트";
    }
}
