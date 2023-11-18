package pl.szlify.codingapi.exceptions;

public class NotYourTeacherException extends RuntimeException {
    public NotYourTeacherException() {
        super("It's not your teacher");
    }
}
