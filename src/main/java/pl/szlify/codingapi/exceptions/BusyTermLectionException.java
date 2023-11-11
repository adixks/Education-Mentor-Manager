package pl.szlify.codingapi.exceptions;

public class BusyTermLectionException extends RuntimeException {
    public BusyTermLectionException() { super("Teacher already has a lesson scheduled at the same time"); }
}
