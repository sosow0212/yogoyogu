package server.yogoyogu.repository.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.likes.Likes;
import server.yogoyogu.entity.member.Member;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberAndBoard(Member member, Board board);
    boolean existsByMemberAndBoard(Member member, Board board);
}
