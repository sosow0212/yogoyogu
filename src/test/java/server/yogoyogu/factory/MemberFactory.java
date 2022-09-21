package server.yogoyogu.factory;

import server.yogoyogu.entity.member.Authority;
import server.yogoyogu.entity.member.Member;

public class MemberFactory {

    public static Member createUser() {
        Member member = Member.builder()
                .username("user")
                .password("user123!")
                .email("user@naver.com")
                .name("이유저")
                .nickname("이유저라오")
                .authority(Authority.ROLE_USER)
                .build();

        return member;
    }

    public static Member createManager() {
        Member member = Member.builder()
                .username("user2")
                .password("user1234!")
                .email("user2@naver.com")
                .name("이학생회")
                .nickname("학생회라오")
                .authority(Authority.ROLE_MANAGER)
                .build();

        return member;
    }
}
