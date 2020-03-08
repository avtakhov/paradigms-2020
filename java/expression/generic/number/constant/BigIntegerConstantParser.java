package expression.generic.number.constant;

import expression.Const;
import expression.generic.number.MyBigInteger;

import java.math.BigInteger;

public class BigIntegerConstantParser implements ConstParser<BigInteger> {
    @Override
    public Const<BigInteger> parse(String expression) {
        return new Const<>(new MyBigInteger(new BigInteger(expression)));
    }
}
