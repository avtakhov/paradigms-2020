package expression;

import expression.generic.number.MyNumber;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression<T> extends ToMiniString {
    MyNumber<T> evaluate(MyNumber<T> x, MyNumber<T> y, MyNumber<T> z);
}
