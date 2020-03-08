package expression.generic;

import expression.generic.number.MyDouble;
import expression.generic.number.constant.DoubleConstantParser;
import expression.parser.ExpressionParser;
import expression.parser.exceptions.ParserException;

public class CustomIntegerTest {

    public static void main(String[] args) throws ParserException {
        ExpressionParser<Double> p = new ExpressionParser<>(new DoubleConstantParser());
        System.out.println(p.parse("1/5").evaluate(new MyDouble(1), new MyDouble(1), new MyDouble(1)).getValue());
    }
}
