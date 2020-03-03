package expression;

import expression.parser.exceptions.NegativeExponentException;
import expression.parser.exceptions.OverflowException;

public class CheckedPow2 extends AbstractUnaryOperator {

    public CheckedPow2(CommonExpression commonExpression) {
        super(commonExpression);
    }

    @Override
    int apply(int x) {
        if (x < 0) {
            throw new NegativeExponentException("Cannot calculate 2^(" + x + ")");
        }
        if (x >= 32) {
            throw new OverflowException("Cannot calculate 2^(" + x + ")");
        }
        return 1 << x;
    }

    @Override
    public int getPriorityLevel() {
        return 20;
    }

    @Override
    public String getOperation() {
        return "pow2 ";
    }
}
