package server.yogoyogu.entity.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.yogoyogu.dto.board.BoardEditRequestDto;
import server.yogoyogu.entity.common.EntityDate;
import server.yogoyogu.entity.member.Member;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Board extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isReplied;

    @Column(nullable = false)
    private Long likesCount;

    public Board(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.isReplied = false;
        this.likesCount = 0L;
    }

    public void editBoard(BoardEditRequestDto req) {
        this.setTitle(req.getTitle());
        this.setContent(req.getContent());
    }

    public void repliedSuccess() {
        this.isReplied = true;
    }

    public void liked() {
        this.likesCount ++;
    }

    public void unliked() {
        this.likesCount --;
    }
}
