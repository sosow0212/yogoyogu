package server.yogoyogu.repository.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import server.yogoyogu.entity.member.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);
}