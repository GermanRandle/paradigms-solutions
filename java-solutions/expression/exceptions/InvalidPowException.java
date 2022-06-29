package expression.exceptions;

public class InvalidPowException extends ArithmeticException {
    public InvalidPowException(String message, int base, int exp) {
        super(message + ": Base = " + base + ", Exponent = " + exp);
    }
}
