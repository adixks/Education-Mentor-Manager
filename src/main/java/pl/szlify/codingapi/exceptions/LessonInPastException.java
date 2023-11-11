package pl.szlify.codingapi.exceptions;

public class LessonInPastException extends RuntimeException {
    public LessonInPastException() { super("Lessons cannot be scheduled in the past"); }
}
