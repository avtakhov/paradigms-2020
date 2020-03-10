package expression.generic.number;

import expression.parser.exceptions.DivideByZeroException;

import java.math.BigInteger;

public class MyBigInteger extends MyNumber<BigInteger> {

    public MyBigInteger(BigInteger x) {
        super(x);
    }

    public MyBigInteger(String s) {
        super(new BigInteger(s));
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
        if (BigInteger.ZERO.equals(other.getValue())) {
            throw new DivideByZeroException("Cannot calculate " + value + " / " + other.value);
        }
        return new MyBigInteger(value.divide(other.value));
    }

    @Override
    public MyNumber<BigInteger> negate() {
        return new MyBigInteger(value.negate());
    }

    @Override
    public MyNumber<BigInteger> min(MyNumber<BigInteger> y) {
        int cmp = value.compareTo(y.value);
        return new MyBigInteger(cmp < 0 ? this.getValue() : y.getValue());
    }

    @Override
    public MyNumber<BigInteger> max(MyNumber<BigInteger> y) {
        int cmp = value.compareTo(y.value);
        return new MyBigInteger(cmp > 0 ? this.getValue() : y.getValue());
    }

    @Override
    public MyNumber<BigInteger> count() {
        return new MyBigInteger(new BigInteger(String.valueOf(value.bitCount())));
    }
}
