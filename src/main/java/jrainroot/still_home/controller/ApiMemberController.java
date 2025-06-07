package jrainroot.still_home.controller;

import jrainroot.still_home.entity.Member;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ApiMemberController {
    private final MemberService memberService;

    @Getter
    public static class LoginRequestBody {
        @NotBlank
        private String name;
        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponseBody {
        private Member loginResponse;
        
    }

    @PostMapping("/login")
    public ResultData<LoginRequestBody> login(@Valid @RequestBody LoginRequestBody loginRequestBody) {
        // username, password => accessToken
        memberService.authAndMakeTokens(loginRequestBody.getName(), loginRequestBody.getPassword());
        return ResultData.of("ok", "ok", null);
    }

    @GetMapping("/test")
    public String memberTest() {
        return "멤버 테스트";
    }
}
