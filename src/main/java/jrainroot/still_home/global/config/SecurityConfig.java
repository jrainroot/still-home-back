package jrainroot.still_home.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration // Spring 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
@RequiredArgsConstructor // Lombok 어노테이션으로, final 로 선언된 필드를 생성자로 자동 주입
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                전체권한 접근
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
//                csrf(Cross-Site Request Forgery) 모든경로 허용
                .csrf(
                        csrf -> csrf
                                .ignoringRequestMatchers("/**")
                )
//                X-Frame-Options: SAMEORIGIN을 설정하여
//                → 같은 도메인 내에서는 <iframe>으로 페이지를 불러올 수 있게 해줍니다.
//                주로 H2 Console을 웹에서 iframe으로 띄우는 문제 해결용입니다.
                .headers(
                        headers -> headers
                                .addHeaderWriter(
                                        new XFrameOptionsHeaderWriter(
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                                        )
                                )
                );
        return http.build();
    }
}
