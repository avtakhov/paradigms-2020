package expression.parser.exceptions;

public class DivideByZeroException extends ExpressionException {
    public DivideByZeroException(String message) {
        super(message);
    }
}
