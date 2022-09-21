package server.yogoyogu.entity.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "email_auth")
@Entity
public class EmailAuth {
    @Id
    @Column(name = "email_key", nullable = false)
    private String key;

    @Column(name = "email", nullable = false)
    private String email;

    public EmailAuth(String key, String email) {
        this.key = key;
        this.email = email;
    }
}
