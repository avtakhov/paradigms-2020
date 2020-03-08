package expression.generic.number;

public class MyDouble extends MyNumber<Double> {
    public MyDouble(double v) {
        super(v);
    }

    public MyDouble(Integer v) {
        super((double) v);
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
}
