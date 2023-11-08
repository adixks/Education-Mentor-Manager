package pl.szlify.codingapi.exceptions;

public class ZajetyTerminLekcjiException extends RuntimeException {
    public ZajetyTerminLekcjiException() { super("Nauczyciel ma już zaplanowaną lekcję w tym samym czasie"); }
}
