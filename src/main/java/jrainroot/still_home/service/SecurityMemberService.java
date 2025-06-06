package jrainroot.still_home.service;

import jakarta.servlet.http.HttpSession;
import jrainroot.still_home.entity.Member;
import jrainroot.still_home.model.MemberRole;
import jrainroot.still_home.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class SecurityMemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // google, naver, kakao
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String email;
        Map<String, Object> response = oAuth2User.getAttributes();

        if(registrationId.equals("naver")) {
            Map<String, Object> hash = (Map<String, Object>) response.get("response");
            email = (String) hash.get("email");
        } else if (registrationId.equals("google")) {
            email = (String) response.get("email");
        } else {
            throw new OAuth2AuthenticationException("허용되지 않은 인증입니다.");
        }

        Member member;
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        // isPresent면 이미 가입된 유저 로그인 진행하면 됨
        if(optionalMember.isPresent()) {
            member = optionalMember.get();
        } else { // 가입되지 않은 유저 회원가입
            member = new Member();
            member.setEmail(email);
            member.setRole(MemberRole.ROLE_USER);
            memberRepository.save(member);
        }

        // 세션에 멤버 등록
        httpSession.setAttribute("member", member);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}
