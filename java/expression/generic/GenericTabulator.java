package expression.generic;

import expression.CommonExpression;
import expression.generic.number.MyBigInteger;
import expression.generic.number.MyDouble;
import expression.generic.number.MyInteger;
import expression.generic.number.MyNumber;
import expression.generic.number.constant.BigIntegerConstantParser;
import expression.generic.number.constant.DoubleConstantParser;
import expression.generic.number.constant.IntegerConstantParser;
import expression.parser.ExpressionParser;
import expression.parser.exceptions.ExpressionException;
import expression.parser.exceptions.ParserException;

import java.math.BigInteger;
import java.util.function.Function;

public class GenericTabulator<T extends Number> implements Tabulator {

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParserException {
        switch (mode) {
            case "i":
                CommonExpression<Integer> commonExpressionInteger = (CommonExpression<Integer>)
                        new ExpressionParser<>(new IntegerConstantParser())
                                .parse(expression);
                return tabulate(this::intApply, commonExpressionInteger, x1, x2, y1, y2, z1, z2);
            case "d":
                CommonExpression<Double> commonExpressionDouble = (CommonExpression<Double>)
                        new ExpressionParser<>(new DoubleConstantParser())
                                .parse(expression);
                return tabulate(this::doubleApply, commonExpressionDouble, x1, x2, y1, y2, z1, z2);
            case "bi":
                CommonExpression<BigInteger> commonExpressionBigInteger = (CommonExpression<BigInteger>)
                        new ExpressionParser<>(new BigIntegerConstantParser())
                                .parse(expression);
                return tabulate(this::bigIntegerApply, commonExpressionBigInteger, x1, x2, y1, y2, z1, z2);
            default:
                throw new IllegalArgumentException("Unknown mode");
        }
    }

    private <E> Object[][][] tabulate(Function<Integer, MyNumber<E>> f, CommonExpression<E> expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int dz = z2 - z1;
        Object[][][] ans = new Object[dx + 1][dy + 1][dz + 1];
        for (int i = 0; i <= dx; i++) {
            for (int j = 0; j <= dy; j++) {
                for (int k = 0; k <= dz; k++) {
                    try {
                        ans[i][j][k] = expression.evaluate(f.apply(x1 + i), f.apply(y1 + j), f.apply(z1 + k)).getValue();
                    } catch (ExpressionException e) {
                        ans[i][j][k] = null;
                    }
                }
            }
        }
        return ans;
    }

    private MyNumber<Integer> intApply(Integer x) {
        return new MyInteger(x);
    }

    private MyNumber<Double> doubleApply(Integer x) {
        return new MyDouble((double) x);
    }

    private MyNumber<BigInteger> bigIntegerApply(Integer x) {
        return new MyBigInteger(new BigInteger(x.toString()));
    }
}
