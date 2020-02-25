package expression.parser.exceptions;

public class NoArgumentException extends ParserException {
    public NoArgumentException(String expected_number) {
        super(expected_number);
    }
}
