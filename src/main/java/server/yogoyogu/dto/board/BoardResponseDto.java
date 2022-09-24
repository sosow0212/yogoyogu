package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.member.Authority;

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
    private String tag;
    private LocalDateTime createdAt;

    public static BoardResponseDto toDto(Board b) {
        return new BoardResponseDto(b.getId(), b.getMember().getName(), b.getTitle(), b.getContent(), b.isReplied(), b.getLikesCount(), tagSelect(b.getTag()), b.getCreatedAt());
    }

    public static String tagSelect(Authority tag) {
        if (tag == Authority.ROLE_ANY) {
            return "none";
        } else if (tag == Authority.ROLE_SEOUL_MANAGER) {
            return "인문캠";
        } else if (tag == Authority.ROLE_YONGIN_MANAGER) {
            return "자연캠";
        } else if (tag == Authority.ROLE_MANAGER) {
            return "총학생회";
        } else {
            return "none";
        }
    }
}
