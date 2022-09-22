package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.board.Board;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardSimpleDto {
    private Long boardId;
    private String title;
    private String writer_name;
    private Long likesCount;
    private Boolean isReplied;

    public static BoardSimpleDto toDto(Board b) {
        return new BoardSimpleDto(b.getId(), b.getTitle(), b.getMember().getName(), b.getLikesCount(), b.isReplied());
    }
}
