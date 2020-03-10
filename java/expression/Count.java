package expression;

import expression.generic.number.MyNumber;

public class Count<T> extends AbstractUnaryOperator<T> {

    public Count(CommonExpression<T> commonExpression) {
        super(commonExpression);
    }

    @Override
    MyNumber<T> apply(MyNumber<T> x) {
        return x.count();
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
