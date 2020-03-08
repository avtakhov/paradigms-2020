package expression.generic.number.constant;

import expression.Const;
import expression.parser.Parser;

public interface ConstParser<T> extends Parser<T> {
    @Override
    Const<T> parse(String expression);
}
