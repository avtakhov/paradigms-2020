package expression.parser.exceptions;

public class NegativeExponentException extends PowException {
    public NegativeExponentException(String message) {
        super(message);
    }
}
