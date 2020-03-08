package expression;

import expression.generic.number.MyNumber;

public interface Expression<T> extends ToMiniString {
    MyNumber<T> evaluate(MyNumber<T> x);
}
