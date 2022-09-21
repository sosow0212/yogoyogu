package server.yogoyogu.entity.member;

import lombok.*;
import server.yogoyogu.entity.common.EntityDate;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String username, String password, String name, String nickname, String email, Authority authority) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
    }
    
}
