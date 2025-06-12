package jrainroot.still_home.service;

import jrainroot.still_home.entity.Member;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.global.jwt.JwtProvider;
import jrainroot.still_home.global.security.SecurityUser;
import jrainroot.still_home.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public Member join(String name, String password, String email) {
        Member member = Member.builder()
        .name(name)
        .password(password)
        .email(email)
        .build();

        String refreshToken = jwtProvider.genRefreshToken(member);
        member.setRefreshToken(refreshToken);

        memberRepository.save(member);
        return member;
    }

    // token 유효성 검사
    public boolean validateToken(String token) {
        return jwtProvider.verify(token);
    }

    public ResultData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리프레시 토큰입니다."));

        String accessToken = jwtProvider.genAccessToken(member);

        return ResultData.of("200-1", "토큰 갱신 성공", accessToken);
    }


    public Optional<Member> findById (Long id) {
        return this.memberRepository.findById(id);
    }


    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokenResponseBody {
        private Member member;
        private String accessToken;
        private String refreshToken;
    }

    @Transactional
    public ResultData<AuthAndMakeTokenResponseBody> authAndMakeTokens(String name, String password) {
        Member member = this.memberRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        
        // 비밀번호 검증 추가
        // System.out.println("getPassword = " + (member.getPassword()));
        // System.out.println("password = " + password);

        // if (!member.getPassword().equals(password)) {
        //     throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        // }
        
        // 시간 설정 및 토큰 생성
        String accessToken = jwtProvider.genAccessToken(member);

        // 리프레시 토큰 생성
        String refreshToken = jwtProvider.genRefreshToken(member);

        return ResultData.of(
            "200-1", 
            "로그인 성공", 
            new AuthAndMakeTokenResponseBody(member, accessToken, refreshToken)
        );
    }

    public SecurityUser getMemberFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);
        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("name");
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new SecurityUser(id, username, "", authorities);
    }

  
}
