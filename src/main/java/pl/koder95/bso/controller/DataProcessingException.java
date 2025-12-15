package pl.koder95.bso.controller;

public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String msg, Exception cause) {
        super(msg, cause);
    }
}
