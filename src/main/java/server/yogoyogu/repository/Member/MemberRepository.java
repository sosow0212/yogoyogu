package server.yogoyogu.repository.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import server.yogoyogu.entity.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByNickname(String nickname);
    public boolean existsByEmail(String Email);

}
