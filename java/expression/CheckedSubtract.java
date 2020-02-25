package expression;

import expression.parser.exceptions.OverflowException;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    protected int apply(int a, int b) {
        if (b > 0 && a < Integer.MIN_VALUE + b || b < 0 && a > Integer.MAX_VALUE + b) {
            throw new OverflowException("Cannot calculate " + a + " - " + b);
        }
        return super.apply(a, b);
    }
}
