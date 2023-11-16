package pl.szlify.codingapi.exceptions;

public class LackOfTeacherException extends RuntimeException {
    public LackOfTeacherException() {
        super("The teacher with the specified ID does not exist");
    }
}
