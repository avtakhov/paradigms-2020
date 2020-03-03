package expression.parser;

import expression.TripleExpression;
import expression.parser.exceptions.ParserException;

public interface Parser {
    TripleExpression parse(String expression) throws ParserException;
}