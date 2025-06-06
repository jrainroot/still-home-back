package jrainroot.still_home.global.initData;

import jrainroot.still_home.entity.Member;
import jrainroot.still_home.service.MemberService;
import jrainroot.still_home.service.PostService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(PostService postService, MemberService memberService, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("1234");
        return args -> {
            // 회원 3명 추가
            Member user1 = memberService.join("이름1", password, "test@email.com");
            Member user2 = memberService.join("이름1", password, "test@email.com");
            Member user3 = memberService.join("이름1", password, "test@email.com");
            Member user4 = memberService.join("이름1", password, "test@email.com");


            // 작성자 회원 추가
            postService.create(user1, "제목 1", "내용 1");
            postService.create(user2, "제목 2", "내용 2");
            postService.create(user3, "제목 3", "내용 3");
            postService.create(user4, "제목 4", "내용 4");
            postService.create(user4, "제목 5", "내용 5");
        };
    }
}
