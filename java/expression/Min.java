package expression;

import expression.generic.number.MyNumber;

public class Min<T> extends AbstractBinaryOperator<T> {

    public Min(CommonExpression<T> first, CommonExpression<T> second) {
        super(first, second);
    }

    @Override
    MyNumber<T> apply(MyNumber<T> x, MyNumber<T> y) {
        return x.min(y);
    }

    @Override
    public int getPriorityLevel() {
        return 1;
    }

    @Override
    public String getOperation() {
        return " min ";
    }
}
