package expression.exceptions;

public class InvalidLogarithmException extends ArithmeticException {
    public InvalidLogarithmException(String message, int base, int value) {
        super(message + ": Base = " + base + ", Value = " + value);
    }
}
