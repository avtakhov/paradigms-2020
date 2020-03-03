package expression;

import expression.parser.exceptions.DivideByZeroException;
import expression.parser.exceptions.OverflowException;

public class CheckedDivide extends Divide {
    public CheckedDivide(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    protected int apply(int a, int b) {
        if (b == 0) {
            throw new DivideByZeroException("Can't calculate " + a + " / " + b);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("Can't calculate " + a + " / " + b);
        }
        return a / b;
    }
}
