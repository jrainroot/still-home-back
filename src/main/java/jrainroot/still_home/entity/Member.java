package jrainroot.still_home.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import jrainroot.still_home.model.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Member extends BaseEntity {

    private String email;
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private MemberRole role;

    // google, kakao
    private String provider;

    // 로그인한 유저의 고유 id
    @Column(name = "provider_id")
    private String providerId;

    private String refreshToken;

    // 권한 체크 
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("MEMBER"));
//        return authorities;
//    }
}
