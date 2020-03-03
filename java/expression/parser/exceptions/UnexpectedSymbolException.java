package expression.parser.exceptions;

public class UnexpectedSymbolException extends ParserException {
    public UnexpectedSymbolException(String s) {
        super(s);
    }
}
