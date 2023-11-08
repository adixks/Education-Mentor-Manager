package pl.szlify.codingapi.exceptions;

public class OdbytaLekcjaException extends RuntimeException {
    public OdbytaLekcjaException() { super("Nie znaleziono lekcji"); }
}
