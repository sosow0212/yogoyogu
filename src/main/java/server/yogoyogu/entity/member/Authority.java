package server.yogoyogu.entity.member;

public enum Authority {
    ROLE_USER, // 일반 유저
    ROLE_SEOUL_MANAGER, // 인문캠 학생회
    ROLE_YONGIN_MANAGER, // 자연캠 학생회
    ROLE_MANAGER, // 총학생회
    ROLE_ADMIN, // 관리자

    ROLE_ANY // 아무나 접근 가능한 태그
}
