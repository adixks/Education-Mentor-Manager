package pl.szlify.codingapi.exceptions;

public class BrakLekcjiException extends RuntimeException {
    public BrakLekcjiException() { super("Nie znaleziono lekcji"); }
}
