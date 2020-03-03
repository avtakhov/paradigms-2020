package expression;

public class Multiply extends AbstractBinaryOperator {

    public Multiply(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    public String getOperation() {
        return " * ";
    }

    @Override
    public int getPriorityLevel() {
        return 6;
    }

    protected int apply(int a, int b) {
        return a * b;
    }
}
