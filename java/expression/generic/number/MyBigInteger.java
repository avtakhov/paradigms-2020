package expression.generic.number;

import expression.parser.exceptions.DivideByZeroException;

import java.math.BigInteger;

public class MyBigInteger extends MyNumber<BigInteger> {

    public MyBigInteger(BigInteger add) {
        super(add);
    }

    public MyBigInteger(Integer add) {
        super(BigInteger.valueOf(add));
    }

    @Override
    public MyNumber<BigInteger> add(MyNumber<BigInteger> other) {
        return new MyBigInteger(value.add(other.value));
    }

    @Override
    public MyNumber<BigInteger> subtract(MyNumber<BigInteger> other) {
        return new MyBigInteger(value.subtract(other.value));
    }

    @Override
    public MyNumber<BigInteger> multiply(MyNumber<BigInteger> other) {
        return new MyBigInteger(value.multiply(other.value));
    }

    @Override
    public MyNumber<BigInteger> divide(MyNumber<BigInteger> other) {
        if (other.value.compareTo(BigInteger.ZERO) == 0) {
            throw new DivideByZeroException("Cannot calculate " + value + " / " + other.value);
        }
        return new MyBigInteger(value.divide(other.value));
    }

    @Override
    public MyNumber<BigInteger> negate() {
        return new MyBigInteger(value.negate());
    }
}
