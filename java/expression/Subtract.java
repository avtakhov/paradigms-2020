package expression;

import expression.generic.number.MyNumber;

public class Subtract<T> extends AbstractBinaryOperator<T> {
    public Subtract(CommonExpression<T> first, CommonExpression<T> second) {
        super(first, second);
    }

    @Override
    MyNumber<T> apply(MyNumber<T> x, MyNumber<T> y) {
        return x.subtract(y);
    }

    @Override
    public String getOperation() {
        return " - ";
    }

    @Override
    public int getPriorityLevel() {
        return 3;
    }

    @Override
    public boolean isSpecialPhrase() {
        return true;
    }
}
