package pl.szlify.codingapi.exceptions;

public class LessonLearnedException extends RuntimeException {
    public LessonLearnedException() { super("Lesson not found or lesson already taught "); }
}
