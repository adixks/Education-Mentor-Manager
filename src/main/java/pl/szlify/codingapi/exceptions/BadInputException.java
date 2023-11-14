package pl.szlify.codingapi.exceptions;

public class BadInputException extends RuntimeException {
    public BadInputException() {
        super("Incorrect data");
    }

    public BadInputException(String message) {
        super(message);
    }
}
