package expression.generic.number.constant;

import expression.Const;
import expression.generic.number.MyInteger;

public class IntegerConstantParser implements ConstParser<Integer> {

    @Override
    public Const<Integer> parse(String expression) {
        return new Const<>(new MyInteger(expression));
    }
}