package expression.parser;

import expression.TripleExpression;
import expression.parser.exceptions.ParserException;

public class Main {
    public static void main(String[] args) throws ParserException {
        Parser parser = new ExpressionParser();
        TripleExpression t = parser.parse("123x");
        System.out.println(t.toMiniString());
        System.out.println(t.evaluate(Integer.MAX_VALUE, 0, 0));
    }
}
