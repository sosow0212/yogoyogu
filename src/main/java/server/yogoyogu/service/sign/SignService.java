package server.yogoyogu.service.sign;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.yogoyogu.config.jwt.TokenProvider;
import server.yogoyogu.dto.sign.LoginRequestDto;
import server.yogoyogu.dto.sign.ReissueRequestDto;
import server.yogoyogu.dto.sign.SignupRequestDto;
import server.yogoyogu.dto.sign.TokenResponseDto;
import server.yogoyogu.dto.token.TokenDto;
import server.yogoyogu.entity.member.Authority;
import server.yogoyogu.entity.member.EmailAuth;
import server.yogoyogu.entity.member.Member;
import server.yogoyogu.entity.member.RefreshToken;
import server.yogoyogu.exception.*;
import server.yogoyogu.repository.Member.EmailAuthRepository;
import server.yogoyogu.repository.Member.MemberRepository;
import server.yogoyogu.repository.Member.RefreshTokenRepository;
import server.yogoyogu.service.email.EmailService;

@RequiredArgsConstructor
@Service
public class SignService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailAuthRepository emailAuthRepository;


    /**
     * 회원가입 순서
     * 1. email 기반 인증번호 입력
     * 2. email + 인증번호를 EmailAuth 테이블에 저장
     * 3. signUp 메서드에서 회원가입시 EmailAuth 가져와서 비교
     */
    @Transactional
    public void signUp(SignupRequestDto req) {

        validateSignUpInfo(req);

        Authority authority;
        if (req.getAuthorityRadio() == 0) {
            authority = Authority.ROLE_USER;
        } else {
            // 학생회
            authority = Authority.ROLE_MANAGER;
        }

        Member member = Member.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .name(req.getName())
                .nickname(req.getNickname())
                .authority(authority)
                .build();

        EmailAuth emailAuth = emailAuthRepository.findEmailAuthByEmail(req.getEmail()).orElseThrow(EmailAuthDosentExistException::new);
        if (emailAuth.getKey().equals(req.getEmailAuthKey())) {
            member.emailPassed();
            memberRepository.save(member);
            emailAuthRepository.delete(emailAuth);
        } else {
            member.emailUnPassed();
            throw new EmailAuthNotEqualsException();
        }
    }

    @Transactional(readOnly = true)
    public boolean confirmMailCode(String code) {
        if(emailAuthRepository.existsByKey(code)) {
            return true;
        } else {
            throw new EmailAuthNotEqualsException();
        }
    }

    @Transactional
    public TokenResponseDto signIn(LoginRequestDto req) {
        validateUsername(req);
        Member member = memberRepository.findByUsername(req.getUsername()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);

        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenResponseDto;
    }

    @Transactional
    public TokenResponseDto reissue(ReissueRequestDto req) {
        if (!tokenProvider.validateToken(req.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(req.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(req.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenResponseDto;
    }

    private void validateSignUpInfo(SignupRequestDto req) {
        if (memberRepository.existsByUsername(req.getUsername())) {
            throw new MemberUsernameAlreadyExistsException(req.getUsername());
        }
        if (memberRepository.existsByNickname(req.getNickname())) {
            throw new MemberNicknameAlreadyExistsException(req.getName());
        }
        if (memberRepository.existsByEmail(req.getEmail())) {
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        }
    }

    private void validateUsername(LoginRequestDto req) {
        if (!memberRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new UsernameNotFoundException();
        }
    }

    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new PasswordNotFoundException();
        }
    }
}

