package nl.moreniekmeijer.lessonplatform.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username " + username + " already exists");
    }
}
