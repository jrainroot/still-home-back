package jrainroot.still_home.service;

import jrainroot.still_home.entity.Member;
import jrainroot.still_home.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member join(String name, String password, String email) {
        Member member = Member.builder()
        .name(name)
        .password(password)
        .email(email)
        .build();

        memberRepository.save(member);
        return member;
    }
}
