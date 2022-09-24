package server.yogoyogu.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.yogoyogu.dto.sign.LoginRequestDto;
import server.yogoyogu.dto.sign.SignupRequestDto;
import server.yogoyogu.entity.member.EmailAuth;
import server.yogoyogu.exception.LoginFailureException;
import server.yogoyogu.exception.PasswordNotFoundException;
import server.yogoyogu.exception.UsernameNotFoundException;
import server.yogoyogu.repository.Member.EmailAuthRepository;
import server.yogoyogu.repository.Member.MemberRepository;
import server.yogoyogu.service.sign.SignService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static server.yogoyogu.factory.MemberFactory.createUser;

@ExtendWith(MockitoExtension.class)
public class SignServiceUnitTest {

    @InjectMocks
    SignService signService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmailAuthRepository emailAuthRepository;

    @Test
    @DisplayName("유저 회원가입 서비스")
    void userSignupTest() {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "dd@naver.com", "mailAuth");
        EmailAuth emailAuth = new EmailAuth("mailAuth", "dd@naver.com");
        given(emailAuthRepository.findEmailAuthByEmail(req.getEmail())).willReturn(Optional.of(emailAuth));

        // when
        signService.signUp(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("메일 인증 확인 테스트")
    void mailConfirmTest() {
        // given
        String code = "1234";
        EmailAuth emailAuth = new EmailAuth(code, "sosow0212@naver.com");
        given(emailAuthRepository.existsByKey(code)).willReturn(true);

        // when
        String result = signService.confirmMailCode(code);

        // then
        assertThat(result).isEqualTo("인증번호가 확인되었습니다.");
    }


    @Test
    @DisplayName("로그인 실패 테스트")
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createUser()));

        // when, then
        assertThatThrownBy(() -> signService.signIn(new LoginRequestDto("email", "password")))
                .isInstanceOf(PasswordNotFoundException.class);
    }

    @Test
    @DisplayName("패스워드 검증 테스트")
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createUser()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new LoginRequestDto("username", "password")))
                .isInstanceOf(PasswordNotFoundException.class);
    }


}



