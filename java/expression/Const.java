package expression;

import expression.generic.number.MyNumber;

import java.util.Objects;

public class Const<T> implements CommonExpression<T> {
    MyNumber<T> c;

    public Const(MyNumber<T> c) {
        this.c = c;
    }

    public String toString() {
        return String.valueOf(c.getValue());
    }

    public String toMiniString() {
        return toString();
    }

    @Override
    public int getPriorityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOperation() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Const<?> constant = (Const<?>) object;
        return Objects.equals(this.c, constant.c);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(c);
    }

    @Override
    public MyNumber<T> evaluate(MyNumber<T> x) {
        return c;
    }

    @Override
    public MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z) {
        return c;
    }
}
