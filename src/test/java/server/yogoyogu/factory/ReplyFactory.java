package server.yogoyogu.factory;

import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.entity.reply.Reply;

public class ReplyFactory {
    public static Reply createReply(Board board, Member member) {
        return new Reply(member, "답변", board);
    }
}
