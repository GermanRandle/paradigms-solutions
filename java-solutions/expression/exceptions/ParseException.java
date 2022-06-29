package expression.exceptions;

public class ParseException extends RuntimeException {
    public ParseException(int position, String message) {
        super("Parsing error on position " + position + ": " + message);
    }
}
