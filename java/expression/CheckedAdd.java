package expression;

import expression.parser.exceptions.OverflowException;

public class CheckedAdd extends Add {
    public CheckedAdd(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    protected int apply(int a, int b) {
        if (a > 0) {
            // a + b > max
            // max - a < b
            if (b > Integer.MAX_VALUE - a) {
                throw new OverflowException("overflow");
            }
        } else if (a < 0) {
            // a + b < min
            // b < min - a
            if (b < Integer.MIN_VALUE - a) {
                throw new OverflowException("overflow");
            }
        }
        return a + b;
    }
}
