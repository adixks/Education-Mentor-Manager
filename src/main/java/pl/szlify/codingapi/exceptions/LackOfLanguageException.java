package pl.szlify.codingapi.exceptions;

public class LackOfLanguageException extends RuntimeException {
    public LackOfLanguageException() {
        super("The Language with the specified name does not exist");
    }
}
