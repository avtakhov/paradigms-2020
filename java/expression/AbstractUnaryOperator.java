package expression;

import expression.generic.number.MyNumber;

import java.util.Objects;

public abstract class AbstractUnaryOperator<T> implements CommonExpression<T> {

    CommonExpression<T> commonExpression;

    protected AbstractUnaryOperator(CommonExpression<T> commonExpression) {
        this.commonExpression = commonExpression;
    }

    abstract MyNumber<T> apply(MyNumber<T> x);

    @Override
    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return apply(commonExpression.evaluate(x, y, z));
    }

    @Override
    public MyNumber<T> evaluate(MyNumber<T> x) {
        return apply(commonExpression.evaluate(x));
    }

    @Override
    public String toString() {
        return "(" + getOperation() + "(" + commonExpression + "))";
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AbstractUnaryOperator<?> unaryOperator = (AbstractUnaryOperator<?>) object;

        return Objects.equals(this.commonExpression, unaryOperator.commonExpression);
    }

    @Override
    public int hashCode() {
        return commonExpression.hashCode() * 100_003 + getClass().hashCode();
    }
}
