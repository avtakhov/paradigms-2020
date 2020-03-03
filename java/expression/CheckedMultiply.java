package expression;

import expression.parser.exceptions.OverflowException;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    protected int apply(int a, int b) {
        int r = a * b;
        if (((b != 0) && (r / b != a)) || (a == Integer.MIN_VALUE && b == -1)) {
            throw new OverflowException("Cannot calculate " + a + " * " + b);
        }
        return r;
    }
}
