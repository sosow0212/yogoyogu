package server.yogoyogu.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.yogoyogu.controller.sign.SignController;
import server.yogoyogu.dto.sign.LoginRequestDto;
import server.yogoyogu.dto.sign.SignupRequestDto;
import server.yogoyogu.dto.sign.TokenResponseDto;
import server.yogoyogu.service.email.EmailService;
import server.yogoyogu.service.sign.SignService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SignControllerUnitTest {
    @InjectMocks
    SignController signController;

    @Mock
    SignService signService;

    @Mock
    EmailService emailService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }

    @Test
    @DisplayName("유저 회원가입")
    void userSignupTest() throws Exception {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "dd@naver.com", 0, "d123");

        // when
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // then
        verify(signService).signUp(req);
    }

    @Test
    @DisplayName("학생회 회원가입")
    void managerSignupTest() throws Exception {
        // given
        SignupRequestDto req = new SignupRequestDto("user", "user123!", "이름", "닉네임", "dd@naver.com", 1, "d123");

        // when
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // then
        verify(signService).signUp(req);
    }

    @Test
    @DisplayName("로그인")
    void signinTest() throws Exception {
        // given
        LoginRequestDto req = new LoginRequestDto("username", "password1!");
        given(signService.signIn(req)).willReturn(new TokenResponseDto("access", "refresh"));

        // when, then
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

        verify(signService).signIn(req);
    }

    @Test
    @DisplayName("유저 회원가입시 이메일 인증")
    void mailConfirmTest() throws Exception {
        // given
        String email = "ss@naver.com";

        // when
        mockMvc.perform(
                post("/api/sign-up/email")
                        .param("email", email)
        ).andExpect(status().isOk());

        // then
        verify(emailService).sendSimpleMessage(email);
    }

    @Test
    @DisplayName("메일 인증번호 확인")
    void mailConfirmCheckTest() throws Exception {
        // given
        String code = "1234";

        // when
        mockMvc.perform(
                post("/api/sign-up/email/check")
                        .param("code", code)
        ).andExpect(status().isOk());

        // then
        verify(signService).confirmMailCode(code);
    }
}
