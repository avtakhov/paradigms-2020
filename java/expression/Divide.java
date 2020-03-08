package expression;

import expression.generic.number.MyNumber;

public class Divide<T> extends AbstractBinaryOperator<T> {

    public Divide(CommonExpression<T> first, CommonExpression<T> second) {
        super(first, second);
    }

    @Override
    public String getOperation() {
        return " / ";
    }

    @Override
    public int getPriorityLevel() {
        return 6;
    }

    protected MyNumber<T> apply(MyNumber<T> a, MyNumber<T> b) {
        return a.divide(b);
    }

    @Override
    public boolean isSpecialPhrase() {
        return true;
    }
}
