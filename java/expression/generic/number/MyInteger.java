package expression.generic.number;

import expression.parser.exceptions.DivideByZeroException;
import expression.parser.exceptions.OverflowException;

public class MyInteger extends MyNumber<Integer> {

    public MyInteger(int i) {
        super(i);
    }

    public MyInteger(String s) {
        super(Integer.parseInt(s));
    }

    @Override
    public MyNumber<Integer> add(MyNumber<Integer> other) {
        int a = value;
        int b = other.value;
        if (a > 0 && b > Integer.MAX_VALUE - a || a < 0 && b < Integer.MIN_VALUE - a) {
            throw new OverflowException("Cannot calculate " + a + " + " + b);
        }
        return new MyInteger(a + b);
    }

    @Override
    public MyNumber<Integer> subtract(MyNumber<Integer> other) {
        int a = value;
        int b = other.value;
        if (b > 0 && a < Integer.MIN_VALUE + b || b < 0 && a > Integer.MAX_VALUE + b) {
            throw new OverflowException("Cannot calculate " + a + " - " + b);
        }
        return new MyInteger(a - b);
    }

    @Override
    public MyNumber<Integer> multiply(MyNumber<Integer> other) {
        int a = value;
        int b = other.value;
        if (b > 0 ? a > Integer.MAX_VALUE / b
                || a < Integer.MIN_VALUE / b
                : (b < -1 ? a > Integer.MIN_VALUE / b
                || a < Integer.MAX_VALUE / b
                : b == -1
                && a == Integer.MIN_VALUE)) {
            throw new OverflowException("Cannot calculate : " + a + "*" + b);
        }
        return new MyInteger(a * b);
    }

    @Override
    public MyNumber<Integer> divide(MyNumber<Integer> other) {
        int a = value;
        int b = other.value;
        if (b == 0) {
            throw new DivideByZeroException("Can't calculate " + a + " / " + b);
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowException("Can't calculate " + a + " / " + b);
        }

        return new MyInteger(a / b);
    }

    @Override
    public MyNumber<Integer> negate() {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("Cannot calculate -" + value);
        }
        return new MyInteger(-value);
    }

    @Override
    public MyNumber<Integer> min(MyNumber<Integer> y) {
        int cmp = value.compareTo(y.value);
        return new MyInteger(cmp < 0 ? this.getValue() : y.getValue());
    }

    @Override
    public MyNumber<Integer> max(MyNumber<Integer> y) {
        int cmp = value.compareTo(y.value);
        return new MyInteger(cmp > 0 ? this.getValue() : y.getValue());
    }

    @Override
    public MyNumber<Integer> count() {
        return new MyInteger(Integer.bitCount(value));
    }
}
