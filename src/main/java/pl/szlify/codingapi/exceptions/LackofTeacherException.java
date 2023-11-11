package pl.szlify.codingapi.exceptions;

public class LackofTeacherException extends RuntimeException {
    public LackofTeacherException() { super("The teacher with the specified ID does not exist"); }
}
