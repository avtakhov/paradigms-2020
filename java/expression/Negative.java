package expression;

public class Negative extends AbstractUnaryOperator {

    public Negative(CommonExpression commonExpression) {
        super(commonExpression);
    }

    @Override
    int apply(int x) {
        return -x;
    }

    @Override
    public int getPriorityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOperation() {
        return "-";
    }
}
