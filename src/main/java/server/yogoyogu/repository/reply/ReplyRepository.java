package server.yogoyogu.repository.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.entity.reply.Reply;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByBoard(Board board);
    boolean existsByBoardAndMember(Board board, Member member);
    boolean existsByBoard(Board board);
}
