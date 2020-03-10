package expression.generic.number;

public class MyDouble extends MyNumber<Double> {
    public MyDouble(double v) {
        super(v);
    }

    public MyDouble(String s) {
        super(Double.parseDouble(s));
    }

    @Override
    public MyNumber<Double> add(MyNumber<Double> other) {
        return new MyDouble(value + other.value);
    }

    @Override
    public MyNumber<Double> subtract(MyNumber<Double> other) {
        return new MyDouble(value - other.value);
    }

    @Override
    public MyNumber<Double> multiply(MyNumber<Double> other) {
        return new MyDouble(value * other.value);
    }

    @Override
    public MyNumber<Double> divide(MyNumber<Double> other) {
        return new MyDouble(value / other.value);
    }

    @Override
    public MyNumber<Double> negate() {
        return new MyDouble(-value);
    }


    @Override
    public MyNumber<Double> min(MyNumber<Double> y) {
        int cmp = value.compareTo(y.value);
        return new MyDouble(cmp < 0 ? this.getValue() : y.getValue());
    }

    @Override
    public MyNumber<Double> max(MyNumber<Double> y) {
        int cmp = value.compareTo(y.value);
        return new MyDouble(cmp > 0 ? this.getValue() : y.getValue());
    }

    @Override
    public MyNumber<Double> count() {
        return new MyDouble(Long.bitCount(Double.doubleToLongBits(value)));
    }
}
