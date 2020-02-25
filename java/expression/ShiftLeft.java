package expression;

public class ShiftLeft extends AbstractBinaryOperator {

    public ShiftLeft(CommonExpression first, CommonExpression second) {
        super(first, second);
    }

    @Override
    int apply(int x, int y) {
        return x << y;
    }

    @Override
    public int getPriorityLevel() {
        return 2;
    }

    @Override
    public String getOperation() {
        return "<<";
    }
}
