package pl.szlify.codingapi.exceptions;

public class BrakNauczycielaException extends RuntimeException {
    public BrakNauczycielaException() { super("Nauczyciel o podanym ID nie istnieje."); }
}
