package jrainroot.still_home.controller;

import jrainroot.still_home.dto.MemberDto;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.global.request.RequestLogin;
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
    private final RequestLogin requestLogin;

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
    public ResultData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody loginRequestBody) {

        ResultData<MemberService.AuthAndMakeTokenResponseBody> authAndMakeTokenResultData
                = memberService.authAndMakeTokens(
                        loginRequestBody.getName(),
                        loginRequestBody.getPassword());

        // 쿠키에 accessToken, refreshToken 토큰 넣기
        requestLogin.setCrossDomainCookie("accessToken",
                authAndMakeTokenResultData.getData().getAccessToken());
        requestLogin.setCrossDomainCookie("refreshToken",
                authAndMakeTokenResultData.getData().getRefreshToken());

        return ResultData.of(
            authAndMakeTokenResultData.getResultCode(),
            authAndMakeTokenResultData.getMsg(),
            new LoginResponseBody(new MemberDto(authAndMakeTokenResultData.getData().getMember())));
    }

    @GetMapping("/me")
    public String me() {
        return "내 정보";
    }

    @PostMapping("/logout")
    public ResultData<Void> logout(HttpServletResponse response) {
        requestLogin.deleteCrossDomainCookie("accessToken");
        requestLogin.deleteCrossDomainCookie("refreshToken");

        return ResultData.of("200", "로그아웃 성공");
    }


}
