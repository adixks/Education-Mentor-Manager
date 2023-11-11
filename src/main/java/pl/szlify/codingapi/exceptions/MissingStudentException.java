package pl.szlify.codingapi.exceptions;

public class MissingStudentException extends RuntimeException {
    public MissingStudentException() { super("The student with the given ID does not exist"); }
}
