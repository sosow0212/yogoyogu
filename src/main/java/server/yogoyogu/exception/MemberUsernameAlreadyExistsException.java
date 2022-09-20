package server.yogoyogu.exception;

public class MemberUsernameAlreadyExistsException extends RuntimeException{
    public MemberUsernameAlreadyExistsException(String message) {
        super(message);
    }
}
