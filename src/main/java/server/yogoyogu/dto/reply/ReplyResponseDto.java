package server.yogoyogu.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.yogoyogu.entity.reply.Reply;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyResponseDto {
    private Long replyId;
    private String writerName;
    private String content;

    public static ReplyResponseDto toDto(Reply r) {
        return new ReplyResponseDto(r.getId(), r.getMember().getName(), r.getContent());
    }
}
