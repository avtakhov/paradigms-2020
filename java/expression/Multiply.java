package expression;

import expression.generic.number.MyNumber;

public class Multiply<T> extends AbstractBinaryOperator<T> {

    public Multiply(CommonExpression<T> first, CommonExpression<T> second) {
        super(first, second);
    }

    @Override
    protected MyNumber<T> apply(MyNumber<T> x, MyNumber<T> y) {
        return x.multiply(y);
    }

    @Override
    public String getOperation() {
        return " * ";
    }

    @Override
    public int getPriorityLevel() {
        return 6;
    }
}
