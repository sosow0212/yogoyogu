package server.yogoyogu.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.yogoyogu.exception.*;
import server.yogoyogu.response.Response;

import javax.management.relation.RoleNotFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    // 500 에러
    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 500 에러
    // 컨버트 실패
    @ExceptionHandler(CannotConvertHelperException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertHelperException e) {
        log.error("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 400 에러
    // validation, MethodArgumentNotValidException
    // 각 검증 어노테이션 별로 지정해놨던 메시지를 응답
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) { // 2
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    // 400
    // 토큰 만료
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response tokenExpiredException() {
        return Response.failure(400, "토큰이 만료되었습니다.");
    }

    // 400 에러
    // Valid 제약조건 위배 캐치
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    // 401 응답
    // 토큰 자격증명 제공X
    @ExceptionHandler(TokenNotInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response tokenNotInvalidException() {
        return Response.failure(401, "토큰의 자격 증명이 유효하지 않습니다.");
    }

    // 401 응답
    // 아이디를 찾을 수 없음
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response usernameNotFoundException() {
        return Response.failure(401, "존재하지 않는 아이디입니다.");
    }

    // 401 응답
    // 비밀번호를 찾을 수 없음
    @ExceptionHandler(PasswordNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response passwordNotFoundException() {
        return Response.failure(401, "존재하지 않는 비밀번호입니다.");
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }

    // 401 응답
    // 유저 정보가 일치하지 않음
    @ExceptionHandler(MemberNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response memberNotEqualsException() {
        return Response.failure(401, "유저 정보가 일치하지 않습니다.");
    }

    // 401 응답
    // 유저 정보가 일치하지 않음
    @ExceptionHandler(EmailAuthNotEqualsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response emailAuthNotEqualsException() {
        return Response.failure(401, "메일 인증 정보가 일치하지 않습니다.");
    }

    // 401 응답
    // 게시글은 학생만 작성 가능
    @ExceptionHandler(BoardOnlyWriteStudentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response boardOnlyWriteStudent() {
        return Response.failure(401, "일반 학생만 건의 게시판에 글을 작성할 수 있습니다.");
    }

    // 401 응답
    // 게시글은 학생회만 작성 가능
    @ExceptionHandler(ReplyOnlyWriteStudentManagerException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response replyOnlyWriteStudentManagerException() {
        return Response.failure(401, "관련 학생회만 답장을 작성할 수 없습니다.");
    }


    // 404 응답
    // 요청한 User를 찾을 수 없음
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return Response.failure(404, "요청한 회원을 찾을 수 없습니다.");
    }


    // 404 응답
    // 요청한 자원을 찾을 수 없음
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return Response.failure(404, "요청한 권한 등급을 찾을 수 없습니다.");
    }

    // 404 응답
    // 요청한 게시글을 찾을 수 없음
    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException() {
        return Response.failure(404, "요청한 게시글을 찾을 수 없습니다.");
    }

    // 404 응답
    // EmailAuth 찾을 수 없음
    @ExceptionHandler(EmailAuthDosentExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response emailAuthDosentExistException() {
        return Response.failure(404, "이메일 인증 키를 찾을 수 없습니다.");
    }

    // 404 응답
    // Reply 찾을 수 없음
    @ExceptionHandler(ReplyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response replyNotFoundException() {
        return Response.failure(404, "답변을 찾을 수 없습니다.");
    }


    // 409 응답
    // username 중복
    @ExceptionHandler(MemberUsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberUsernameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 아이디 입니다.");
    }

    // 409 응답
    // nickname 중복
    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 닉네임 입니다.");
    }

    // 409 응답
    // email 중복
    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 이메일 입니다.");
    }

    // 409 응답
    // phone 중복
    @ExceptionHandler(MemberPhoneAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberPhoneAlreadyExistsException(MemberPhoneAlreadyExistsException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 번호 입니다.");
    }

    // 409 응답
    // 답변 중복
    @ExceptionHandler(ReplyAlreadyWroteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response replyAlreadyWroteException() {
        return Response.failure(409, "이미 답변을 작성하셨습니다.");
    }



}
