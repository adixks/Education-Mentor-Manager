package pl.szlify.codingapi.exceptions;

public class NoLessonsException extends RuntimeException {
    public NoLessonsException() { super("Lessons not found"); }
}
