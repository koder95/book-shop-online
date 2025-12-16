package pl.koder95.bso.exception;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
