package jrainroot.still_home.global.request;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jrainroot.still_home.entity.Member;
import jrainroot.still_home.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class RequestLogin {
    private final HttpServletResponse resp;
    private final HttpServletRequest req;
    private Member member;
    private final EntityManager em;

    public String getCookie(String name) {
        Cookie[] cookies = req.getCookies();

        if (cookies == null) return "";

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }

    public void setCrossDomainCookie(String tokenName, String token) {
        ResponseCookie cookie =  ResponseCookie.from(tokenName, token)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true) // 프론트에서 직접적인 접근이 안되어 보안상 좋음
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public Member getMember() {
        if (isLogout()) return null;

        if(member == null) {
            member = em.getReference(Member.class, getUser().getId());
        }
        return member;
    }

    public void setLogin(SecurityUser securityUser) {
        SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
    }

    private SecurityUser getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(context -> context.getAuthentication())
                .filter(authentication -> authentication.getPrincipal() instanceof SecurityUser)
                .map(authentication -> (SecurityUser) authentication.getPrincipal())
                .orElse(null);
    }

    private boolean isLogin() {
        return getUser() != null;
    }

    private boolean isLogout() {
        return !isLogin();
    }
}
