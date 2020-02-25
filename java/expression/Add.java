package expression;

public class Add extends AbstractBinaryOperator {

    public Add(CommonExpression first, CommonExpression second) {
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
    protected int apply(int x, int y) {
        return x + y;
    }
}
