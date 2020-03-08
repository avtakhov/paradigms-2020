package expression;

import expression.generic.number.MyNumber;

import java.util.Objects;

public abstract class AbstractBinaryOperator<T> implements CommonExpression<T> {
    private CommonExpression<T> first;
    private CommonExpression<T> second;

    protected AbstractBinaryOperator(CommonExpression<T> first, CommonExpression<T> second) {
        this.first = first;
        this.second = second;
    }

    private String putBrackets(CommonExpression<T> expression, boolean isBracket) {
        return (isBracket ? "(" : "") + expression.toMiniString() + (isBracket ? ")" : "");
    }

    private boolean checkBrackets(CommonExpression<T> expression) {
        return expression.getPriorityLevel() < this.getPriorityLevel();
    }

    private boolean checkSpecialBrackets(CommonExpression<T> expression) {
        return expression.isSpecialPhrase() && this.getPriorityLevel() == expression.getPriorityLevel()
                || this.isSpecialPhrase() && expression.getPriorityLevel() <= this.getPriorityLevel();

    }

    @Override
    public String toMiniString() {
        return putBrackets(first, checkBrackets(first)) +
                getOperation() +
                putBrackets(second, checkBrackets(second) || checkSpecialBrackets(second));
    }

    abstract MyNumber<T> apply(MyNumber<T> x, MyNumber<T> y);

    @Override
    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return apply(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public MyNumber<T> evaluate(MyNumber<T> x) {
        return apply(first.evaluate(x), second.evaluate(x));
    }

    @Override
    public String toString() {
        return "(" + first + getOperation() + second + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        AbstractBinaryOperator<?> binaryOperator = (AbstractBinaryOperator<?>) object;

        return Objects.equals(this.first, binaryOperator.first)
                && Objects.equals(this.second, binaryOperator.second);
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 100_003 + second.hashCode() * 103 + getClass().hashCode() * 2 + 1;
    }
}
