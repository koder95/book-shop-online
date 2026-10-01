package pl.koder95.bso.exception;

public class AuthenticatedUserException extends RuntimeException {
    public AuthenticatedUserException(String message) {
        super(message);
    }

    public AuthenticatedUserException() {
        this("requires authenticated user");
    }
}
