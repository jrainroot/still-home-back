package jrainroot.still_home.entity;

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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

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
}
