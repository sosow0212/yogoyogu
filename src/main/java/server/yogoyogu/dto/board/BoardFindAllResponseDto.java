package server.yogoyogu.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.board.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardFindAllResponseDto {
    private List<BoardSimpleDto> boards;
    private PageInfoDto pageInfo;

    public static BoardFindAllResponseDto toDto(List<BoardSimpleDto> boards, PageInfoDto pageInfo) {
        return new BoardFindAllResponseDto(boards ,pageInfo);
    }
}
