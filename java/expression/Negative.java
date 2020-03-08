package expression;

import expression.generic.number.MyNumber;

public class Negative<T> extends AbstractUnaryOperator<T> {

    public Negative(CommonExpression<T> commonExpression) {
        super(commonExpression);
    }

    @Override
    MyNumber<T> apply(MyNumber<T> x) {
        return x.negate();
    }

    @Override
    public int getPriorityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOperation() {
        return "-";
    }
}
