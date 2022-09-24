package server.yogoyogu.entity.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.yogoyogu.dto.board.BoardEditRequestDto;
import server.yogoyogu.entity.common.EntityDate;
import server.yogoyogu.entity.member.Authority;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority tag; // ROLE_ANY, ROLE_SEOUL, ROLE_YONGIN, ROLE_ADMIN (말머리)

    public Board(Member member, String title, String content, Authority tag) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.isReplied = false;
        this.likesCount = 0L;
        this.tag = tag;
    }

    public void editBoard(BoardEditRequestDto req) {
        this.setTitle(req.getTitle());
        this.setContent(req.getContent());
        this.setTag(selectTag(req.getTag()));
    }

    public Authority selectTag(String tag) {
        if (tag.equals("none")) {
            return Authority.ROLE_ANY;
        } else if (tag.equals("인문캠")) {
            return Authority.ROLE_SEOUL_MANAGER;
        } else if (tag.equals("자연캠")) {
            return Authority.ROLE_YONGIN_MANAGER;
        } else if (tag.equals("총학생회")) {
            return Authority.ROLE_MANAGER;
        } else {
            return Authority.ROLE_ANY;
        }
    }

    public void repliedSuccess() {
        this.isReplied = true;
    }

    public void liked() {
        this.likesCount++;
    }

    public void unliked() {
        this.likesCount--;
    }
}
