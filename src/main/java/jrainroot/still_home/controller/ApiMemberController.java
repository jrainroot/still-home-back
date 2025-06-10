package jrainroot.still_home.controller;

import jrainroot.still_home.dto.MemberDto;
import jrainroot.still_home.entity.Member;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
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
        private MemberDto memberDto;
    }

    @PostMapping("/login")
    public ResultData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody loginRequestBody, HttpServletResponse resp) {
        // username, password => accessToken
        ResultData<MemberService.AuthAndMakeTokenResponseBody> authAndMakeTokenResultData = memberService.authAndMakeTokens(
                loginRequestBody.getName(),
                loginRequestBody.getPassword());

        ResponseCookie cookie =  ResponseCookie.from("accessToken", authAndMakeTokenResultData.getData().getAccessToken())
            .path("/")
            .sameSite("None")
            .secure(true)
            .httpOnly(true) // 프론트에서 직접적인 접근이 안되어 보안상 좋음 
            .build();
        resp.addHeader("Set-Cookie", cookie.toString());    
        
        // header에 token 추가
        // resp.addHeader("Set-Cookie", authAndMakeTokenResultData.getData().getAccessToken());
        
        return ResultData.of(
            authAndMakeTokenResultData.getResultCode(),
            authAndMakeTokenResultData.getMsg(),
            new LoginResponseBody(new MemberDto(authAndMakeTokenResultData.getData().getMember())));
    }

    @GetMapping("/me")
    public String me() {
        return "내 정보";
    }

    @GetMapping("/test")
    public String memberTest() {
        return "멤버 테스트";
    }
}
