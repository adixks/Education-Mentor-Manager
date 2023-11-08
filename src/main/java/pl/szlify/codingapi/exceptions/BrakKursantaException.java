package pl.szlify.codingapi.exceptions;

public class BrakKursantaException extends RuntimeException {
    public BrakKursantaException() { super("Kursanta o podanym ID nie istnieje."); }
}
