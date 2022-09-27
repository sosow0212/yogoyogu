package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.dto.reply.ReplyResponseDto;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.entity.reply.Reply;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardAndReplyResponseDto {

    private BoardResponseDto boards;
    private ReplyResponseDto reply;
    private Boolean isPermittedToReplyOrEdit;
    private Boolean isAlreadyPushedLikeByUser;
    private Boolean isMineBoard;

    public static BoardAndReplyResponseDto toDto(Board board, Reply reply, Member member, boolean isAlreadyPushedLikeByUser, boolean isMineBoard) {
        if (reply == null) {
            return new BoardAndReplyResponseDto(new BoardResponseDto().toDto(board), null, member.getAuthority().equals(board.getTag()), isAlreadyPushedLikeByUser, isMineBoard);
        } else {
            return new BoardAndReplyResponseDto(new BoardResponseDto().toDto(board), new ReplyResponseDto().toDto(reply), member.getAuthority().equals(board.getTag()), isAlreadyPushedLikeByUser, isMineBoard);
        }
    }
}
