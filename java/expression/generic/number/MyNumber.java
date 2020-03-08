package expression.generic.number;

public abstract class MyNumber<T> {
    protected T value;

    MyNumber(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public abstract MyNumber<T> add(MyNumber<T> other);

    public abstract MyNumber<T> subtract(MyNumber<T> other);

    public abstract MyNumber<T> multiply(MyNumber<T> other);

    public abstract MyNumber<T> divide(MyNumber<T> other);

    public abstract MyNumber<T> negate();

}

