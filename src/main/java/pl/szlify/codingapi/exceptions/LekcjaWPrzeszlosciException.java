package pl.szlify.codingapi.exceptions;

public class LekcjaWPrzeszlosciException extends RuntimeException {
    public LekcjaWPrzeszlosciException() { super("Nie można zaplanować lekcji w przeszłości"); }
}
