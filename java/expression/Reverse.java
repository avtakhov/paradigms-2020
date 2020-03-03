package expression;

public class Reverse extends AbstractUnaryOperator {

    public Reverse(CommonExpression commonExpression) {
        super(commonExpression);
    }

    @Override
    int apply(int x) {
        int y = 0;
        while (x != 0) {
            y = 10 * y + x % 10;
            x /= 10;
        }
        return y;
    }

    @Override
    public int getPriorityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOperation() {
        return "reverse ";
    }
}
