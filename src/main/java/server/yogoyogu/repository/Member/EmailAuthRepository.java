package server.yogoyogu.repository.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import server.yogoyogu.entity.member.EmailAuth;

import java.util.List;
import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findEmailAuthByEmail(String email);
    Optional<EmailAuth> findEmailAuthByKey(String key);
    boolean existsByEmail(String email);
    boolean existsByKey(String key);
    void deleteByEmail(String email);
}
