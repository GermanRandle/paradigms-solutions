package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    char stepBack();
    IllegalArgumentException error(final String message);
}