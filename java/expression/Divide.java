package expression;

public class Divide extends AbstractBinaryOperator {

    public Divide(CommonExpression first, CommonExpression second) {
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

    protected int apply(int a, int b) {
        return a / b;
    }

    @Override
    public boolean isSpecialPhrase() {
        return true;
    }
}
