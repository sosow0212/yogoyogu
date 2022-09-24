package server.yogoyogu.factory;

import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.member.Authority;
import server.yogoyogu.entity.member.Member;

public class BoardFactory {
    public static Board createBoard(Member member) {
        return new Board(member, "제목", "내용", Authority.ROLE_ANY);
    }
}
