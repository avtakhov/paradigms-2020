package expression;

public class Subtract extends AbstractBinaryOperator {
    public Subtract(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    public String getOperation() {
        return " - ";
    }

    @Override
    public int getPriorityLevel() {
        return 3;
    }

    protected int apply(int a, int b) {
        return a - b;
    }

    @Override
    public boolean isSpecialPhrase() {
        return true;
    }
}
