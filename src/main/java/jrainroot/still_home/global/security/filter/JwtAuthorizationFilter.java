package jrainroot.still_home.global.security.filter;

import java.io.IOException;
import java.util.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jrainroot.still_home.global.ResultData.ResultData;
import jrainroot.still_home.global.request.RequestLogin;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jrainroot.still_home.global.security.SecurityUser;
import jrainroot.still_home.service.MemberService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter{
    private final MemberService memberService;
    private final RequestLogin requestLogin;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request.getRequestURI().equals("/api/members/login") || request.getRequestURI().equals("/api/members/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken을 쿠키에서 가져올거임 
        String accessToken = requestLogin.getCookie("accessToken");
        // accessToken 검증 , refreshToken 발급
        if (!accessToken.isBlank()) {
            // 토큰 유효기간이 지났을 때(문제가 있을 때)
            if(!memberService.validateToken(accessToken)) {
                String refreshToken = requestLogin.getCookie("refreshToken");
                ResultData<String> resultData = memberService.refreshAccessToken(refreshToken);
                requestLogin.setCrossDomainCookie("accessToken", resultData.getData());
            }
            // accessToken을 이용해서 securityUser를 가져온다.
            SecurityUser securityUser = memberService.getMemberFromAccessToken(accessToken);
            // 로그인 처리
            requestLogin.setLogin(securityUser);
        }

        filterChain.doFilter(request, response);
    }

    // 아래거 안쓰고 위에걸로 쓰네?

    // private final JwtProvider jwtProvider;
    // private final MemberService memberService;

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //     // 헤더에서 Authorization 값을 가져온다.
    //     String bearerToken = request.getHeader("Authorization");

    //     if (bearerToken != null) {
    //         // Bearer 띄우고 어쩌고 값이 있는지 확인 후 가져옴 
    //         String token = bearerToken.substring("Bearer ".length());

    //         // 토큰의 유효성을 검증
    //         if (jwtProvider.verify(token)) {
    //             // 유효성에 문제가 없으면 그 안에 있는 Claim(회원) 정보를 가져와서 id를 가져옴
    //             Map<String, Object> claims = jwtProvider.getClaims(token);
    //             long id = (int)claims.get("id");

    //             // 그 id에 해당하는 회원이 있는지 조회
    //             Member member = memberService.findById(id).orElseThrow();

    //             // 권한에 member를 추가
    //             forceAuthentication(member);
    //         }
    //     }

    //     filterChain.doFilter(request, response);
    // }

    // // 강제로 로그인 처리하는 메서드
    // private void forceAuthentication(Member member) {
    //     User user = new User(member.getName(), member.getPassword(), member.getAuthorities());

    //     // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
    //     UsernamePasswordAuthenticationToken authentication = 
    //         UsernamePasswordAuthenticationToken.authenticated(
    //             user, 
    //             null, 
    //             member.getAuthorities()
    //         );

    //     // 스프링 시큐리티 내에 우리가 만든 authentication 객체를 저장할 context 생성
    //     SecurityContext context = SecurityContextHolder.createEmptyContext();

    //     // context에 authentication 객체를 저장
    //     context.setAuthentication(authentication);

    //     // 스프링 시큐리티에 context를 등록
    //     SecurityContextHolder.setContext(context);
    // }

}
