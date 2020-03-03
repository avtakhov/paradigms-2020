package expression;

public class Digits extends AbstractUnaryOperator {

    public Digits(CommonExpression commonExpression) {
        super(commonExpression);
    }

    @Override
    int apply(int x) {
        int ans = 0;
        while (x != 0) {
            ans += x % 10;
            x /= 10;
        }
        return Math.abs(ans);
    }

    @Override
    public int getPriorityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOperation() {
        return "digits ";
    }
}
