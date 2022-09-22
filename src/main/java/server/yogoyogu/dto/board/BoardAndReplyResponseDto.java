package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.dto.reply.ReplyResponseDto;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.reply.Reply;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardAndReplyResponseDto {

    private BoardResponseDto boards;
    private ReplyResponseDto reply;

    public static BoardAndReplyResponseDto toDto(Board board, Reply reply) {
        return new BoardAndReplyResponseDto(new BoardResponseDto().toDto(board), new ReplyResponseDto().toDto(reply));
    }
}
