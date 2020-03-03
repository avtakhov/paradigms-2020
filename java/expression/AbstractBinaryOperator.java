package expression;

import java.util.Objects;

public abstract class AbstractBinaryOperator implements CommonExpression {
    private CommonExpression first;
    private CommonExpression second;

    protected AbstractBinaryOperator(CommonExpression first, CommonExpression second) {
        this.first = first;
        this.second = second;
    }

    private String putBrackets(CommonExpression expression, boolean isBracket) {
        return (isBracket ? "(" : "") + expression.toMiniString() + (isBracket ? ")" : "");
    }

    private boolean checkBrackets(CommonExpression expression) {
        return expression.getPriorityLevel() < this.getPriorityLevel();
    }

    private boolean checkSpecialBrackets(CommonExpression expression) {
        return expression.isSpecialPhrase() && this.getPriorityLevel() == expression.getPriorityLevel()
                || this.isSpecialPhrase() && expression.getPriorityLevel() <= this.getPriorityLevel();

    }

    @Override
    public String toMiniString() {
        return putBrackets(first, checkBrackets(first)) +
                getOperation() +
                putBrackets(second, checkBrackets(second) || checkSpecialBrackets(second));
    }

    abstract int apply(int x, int y);

    @Override
    public int evaluate(int x, int y, int z) {
        return apply(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) {
        return apply(first.evaluate(x), second.evaluate(x));
    }

    @Override
    public String toString() {
        return "(" + first + getOperation() + second + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AbstractBinaryOperator binaryOperator = (AbstractBinaryOperator) object;

        return Objects.equals(this.first, binaryOperator.first)
                && Objects.equals(this.second, binaryOperator.second);
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 100_003 + second.hashCode() * 103 + getClass().hashCode() * 2 + 1;
    }
}
