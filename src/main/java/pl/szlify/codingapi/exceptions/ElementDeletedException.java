package pl.szlify.codingapi.exceptions;

public class ElementDeletedException extends RuntimeException {
    public ElementDeletedException() {
        super("The person has been removed");
    }
}
