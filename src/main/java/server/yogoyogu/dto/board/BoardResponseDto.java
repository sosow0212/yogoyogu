package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.board.Board;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardResponseDto {
    private Long boardId;
    private String writerName;
    private String title;
    private String content;
    private Boolean isReplied;
    private Long likesCount;
    private LocalDateTime createdAt;

    public static BoardResponseDto toDto(Board b) {
        return new BoardResponseDto(b.getId(), b.getMember().getName(), b.getTitle(), b.getContent(), b.isReplied(), b.getLikesCount(), b.getCreatedAt());
    }
}
