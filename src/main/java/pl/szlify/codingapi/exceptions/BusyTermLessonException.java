package pl.szlify.codingapi.exceptions;

public class BusyTermLessonException extends RuntimeException {
    public BusyTermLessonException() {
        super("Teacher already has a lesson scheduled at the same time");
    }
}
