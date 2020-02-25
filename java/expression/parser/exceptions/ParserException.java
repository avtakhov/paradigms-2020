package expression.parser.exceptions;

public abstract class ParserException extends Exception {
    public ParserException(final String message) {
        super(message);
    }
}
