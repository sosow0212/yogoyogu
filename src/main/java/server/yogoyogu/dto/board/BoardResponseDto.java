package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.board.Board;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardResponseDto {
    private Long boardId;
    private String writer_name;
    private String title;
    private String content;
    private Boolean isReplied;
    private Long likesCount;

    public static BoardResponseDto toDto(Board b) {
        return new BoardResponseDto(b.getId(), b.getMember().getName(), b.getTitle(), b.getContent(), b.isReplied(), b.getLikesCount());
    }
}
