package nl.moreniekmeijer.lessonplatform.exceptions;

public class InvalidInviteCodeException extends RuntimeException {
    public InvalidInviteCodeException(String message) {
        super(message);
    }
}
