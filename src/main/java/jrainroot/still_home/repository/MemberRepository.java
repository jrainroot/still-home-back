package jrainroot.still_home.repository;

import jrainroot.still_home.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    Optional<Member> findByRefreshToken(String refreshToken);
}
