package expression;

import expression.generic.number.MyNumber;

public class Max<T> extends AbstractBinaryOperator<T> {

    public Max(CommonExpression<T> first, CommonExpression<T> second) {
        super(first, second);
    }

    @Override
    MyNumber<T> apply(MyNumber<T> x, MyNumber<T> y) {
        return x.max(y);
    }

    @Override
    public int getPriorityLevel() {
        return 1;
    }

    @Override
    public String getOperation() {
        return " max ";
    }
}
