package pl.szlify.codingapi.exceptions;

public class LessonInFutureException extends RuntimeException {
    public LessonInFutureException() {
        super("Person with this id have lesson in the future");
    }
}
