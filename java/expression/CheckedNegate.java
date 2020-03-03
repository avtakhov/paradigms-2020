package expression;

import expression.parser.exceptions.OverflowException;

public class CheckedNegate extends Negative {
    public CheckedNegate(CommonExpression commonExpression) {
        super(commonExpression);
    }

    @Override
    int apply(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("cannot calculate -(" + x + ")");
        }
        return -x;
    }
}
