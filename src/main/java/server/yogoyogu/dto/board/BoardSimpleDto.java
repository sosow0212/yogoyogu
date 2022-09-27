package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.board.Board;
import server.yogoyogu.entity.member.Authority;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardSimpleDto {
    private Long boardId;
    private String title;
    private String writer_name;
    private Long likesCount;
    private Boolean isAlreadyPushedLikeByUser;
    private Boolean isReplied;
    private String tag;

    public static BoardSimpleDto toDto(Board b, boolean isAlreadyPushedLikeByUser) {
        return new BoardSimpleDto(b.getId(), b.getTitle(), b.getMember().getName(), b.getLikesCount(), isAlreadyPushedLikeByUser, b.isReplied(), tagSelect(b.getTag()));
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
