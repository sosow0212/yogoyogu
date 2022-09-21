package server.yogoyogu.controller.sign;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.yogoyogu.dto.sign.LoginRequestDto;
import server.yogoyogu.dto.sign.ReissueRequestDto;
import server.yogoyogu.dto.sign.SignupRequestDto;
import server.yogoyogu.response.Response;
import server.yogoyogu.service.email.EmailService;
import server.yogoyogu.service.sign.SignService;

import javax.validation.Valid;

@Api(value = "Sign Controller", tags = "Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SignController {

    private final SignService signService;
    private final EmailService emailService;

    @ApiOperation(value = "유저 회원가입", notes = "유저 회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public Response userSignup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        signService.signUp(signupRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "유저 회원가입시 이메일 인증번호 전송", notes = "유저 회원가입시 이메일 인증번호 전송")
    @PostMapping("/sign-up/email")
    @ResponseStatus(HttpStatus.OK)
    public Response mailConfirm(@RequestParam String email) throws Exception {
        return Response.success(emailService.sendSimpleMessage(email));
    }

    @ApiOperation(value = "유저 회원가입시 이메일 인증 코드확인", notes = "유저 회원가입시 이메일 인증 코드확인")
    @PostMapping("/sign-up/email/check")
    @ResponseStatus(HttpStatus.OK)
    public Response mailConfirmCheck(@RequestParam String code) {
        return Response.success(signService.confirmMailCode(code));
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody LoginRequestDto req) {
        return Response.success(signService.signIn(req));
    }


    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        return Response.success(signService.reissue(reissueRequestDto));
    }


}
