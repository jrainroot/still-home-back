package jrainroot.still_home.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jrainroot.still_home.global.security.filter.JwtAuthorizationFilter;

// api 경로로 들어오는것만 따로 관리하기 위해 만든 파일
// 권한 체크가 필요한 경우 로그인 했는지 체크 해야함
@Configuration // Spring 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
@RequiredArgsConstructor // Lombok 어노테이션으로, final 로 선언된 필드를 생성자로 자동 주입
public class ApiSecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(
                        (authorizeRequests) -> authorizeRequests
                                .requestMatchers("/api/posts").permitAll()   // 전체글 보기는 누구나 가능
                                .requestMatchers("/api/posts/*").permitAll() // 게시글 상세 보기는 누구나 가능
                                .requestMatchers("/api/members/login").permitAll() // 로그인은 누구나 가능
                                // .requestMatchers(HttpMethod.POST, "/api/members/login").permitAll() // 이런식으로 HttpMethod만 허용핧 수 있음 
                                
                                .anyRequest().authenticated() // 위 조건 외에는 전부 인증된 처리자만 가능하도록 
                )
                .cors( // cors 설정, 타 도메인에서 api 호출 가능
                        cors -> cors.disable()
                )
                // login은 jwt 방식으로만 처리하기 위해 나머진 disable처리
                .csrf( // csrf 토근 끄기
                        csrf -> csrf.disable()
                )
                .httpBasic( // httpBasic 로그인 방식 끄기
                        httpBasic -> httpBasic.disable()
                )
                .formLogin( // form 로그인 방식 끄기
                        formLogin -> formLogin.disable()
                )
                .sessionManagement( // session 끄기 
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtAuthorizationFilter, // 엑세스 토큰을 이용한 로그인 처리
                        UsernamePasswordAuthenticationFilter.class
                )
                ;
                return http.build();
    }
}
