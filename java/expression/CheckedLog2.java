package expression;

import expression.parser.exceptions.NotPositiveLogArgument;

public class CheckedLog2 extends AbstractUnaryOperator {

    public CheckedLog2(CommonExpression commonExpression) {
        super(commonExpression);
    }

    @Override
    int apply(int x) {
        if (x <= 0) {
            throw new NotPositiveLogArgument("cant calculate log2(" + x + ")");
        }
        int ans = 0;
        while (x != 0) {
            x /= 2;
            ans++;
        }
        return ans - 1;
    }

    @Override
    public int getPriorityLevel() {
        return 20;
    }

    @Override
    public String getOperation() {
        return "log2 ";
    }
}
