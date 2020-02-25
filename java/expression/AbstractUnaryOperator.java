package expression;

import java.util.Objects;

public abstract class AbstractUnaryOperator implements CommonExpression {

    CommonExpression commonExpression;

    protected AbstractUnaryOperator(CommonExpression commonExpression) {
        this.commonExpression = commonExpression;
    }

    abstract int apply(int x);

    @Override
    public int evaluate(int x, int y, int z) {
        return apply(commonExpression.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) {
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
        AbstractUnaryOperator unaryOperator = (AbstractUnaryOperator) object;

        return Objects.equals(this.commonExpression, unaryOperator.commonExpression);
    }

    @Override
    public int hashCode() {
        return commonExpression.hashCode() * 100_003 + getClass().hashCode();
    }
}
