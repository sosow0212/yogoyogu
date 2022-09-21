package server.yogoyogu.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import server.yogoyogu.entity.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
