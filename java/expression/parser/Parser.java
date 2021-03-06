package expression.parser;

import expression.TripleExpression;
import expression.parser.exceptions.ParserException;

public interface Parser<T> {
    TripleExpression<T> parse(String expression) throws ParserException;
}