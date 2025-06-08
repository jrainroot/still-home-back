package jrainroot.still_home.service;

import jrainroot.still_home.entity.Member;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.global.jwt.JwtProvider;
import jrainroot.still_home.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

        memberRepository.save(member);
        return member;
    }

    public Optional<Member> findById (Long id) {
        return this.memberRepository.findById(id);
    }


    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokenResponseBody {
        private Member member;
        private String accessToken;
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
        String accessToken = jwtProvider.genToken(member, 60 * 60 * 5);

        // System.out.println("accessToke = " + accessToken);
        return ResultData.of(
            "200-1", 
            "로그인 성공", 
            new AuthAndMakeTokenResponseBody(member, accessToken)
        );
    }
}
