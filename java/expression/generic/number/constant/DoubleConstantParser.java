package expression.generic.number.constant;

import expression.Const;
import expression.generic.number.MyDouble;

public class DoubleConstantParser implements ConstParser<Double> {
    @Override
    public Const<Double> parse(String expression) {
        return new Const<>(new MyDouble(expression));
    }
}
