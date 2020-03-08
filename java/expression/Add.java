package expression;

import expression.generic.number.MyNumber;

public class Add<T> extends AbstractBinaryOperator<T> {

    public Add(CommonExpression<T> first, CommonExpression<T> second) {
        super(first, second);
    }

    @Override
    public String getOperation() {
        return " + ";
    }

    @Override
    public int getPriorityLevel() {
        return 3;
    }

    @Override
    protected MyNumber<T> apply(MyNumber<T> x, MyNumber<T> y) {
        return x.add(y);
    }
}
