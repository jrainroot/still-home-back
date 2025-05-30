package jrainroot.still_home.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// api 경로로 들어오는것만 따로 관리하기 위해 만든 파일
// 권한 체크가 필요한 경우 로그인 했는지 체크 해야함
@Configuration // Spring 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
@RequiredArgsConstructor // Lombok 어노테이션으로, final 로 선언된 필드를 생성자로 자동 주입
public class ApiSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(
                        (authorizeRequests) -> authorizeRequests
                                .requestMatchers("/api/posts").permitAll()
                                .requestMatchers("/api/posts/*").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(
                        csrf -> csrf
                                .disable()
                );
                return http.build();
    }
}
