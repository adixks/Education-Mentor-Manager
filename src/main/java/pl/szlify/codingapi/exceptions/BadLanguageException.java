package pl.szlify.codingapi.exceptions;

public class BadLanguageException extends RuntimeException {
    public BadLanguageException() { super("Teacher does not teach this language"); }
}
