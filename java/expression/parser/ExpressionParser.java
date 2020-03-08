package expression.parser;

import expression.*;
import expression.generic.number.constant.ConstParser;
import expression.parser.exceptions.*;

import java.util.Map;
import java.util.Objects;

public class ExpressionParser<T> extends BaseParser implements Parser<T> {
    private static final Map<String, Integer> PRIORITIES = Map.of(
            "+", 1,
            "-", 1,
            "*", 2,
            "/", 2,
            "<<", 0,
            ">>", 0,
            "log2", -1,
            "pow2", -1
    );
    final int LAST_PRIORITY = 3;
    final int START_PRIORITY = 0;
    private String last = "";
    private final ConstParser<T> constParser;

    public ExpressionParser(ConstParser<T> constParser) {
        this.constParser = constParser;
    }

    @Override
    public TripleExpression<T> parse(String expression) throws ParserException {
        setSource(new StringSource(expression));
        CommonExpression<T> res = parseOperation(START_PRIORITY);
        if (hasNext()) {
            throw new UnexpectedSymbolException(getPos() + ": " + ch);
        }
        return res;
    }

    private String testWordOperation() {
        skipWhitespaces();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < BUFFER_SIZE && (Character.isLetter(buffer[i]) || Character.isDigit(buffer[i]) || buffer[i] == '_')) {
            sb.append(buffer[i]);
            i++;
        }
        String s = sb.toString();
        if (PRIORITIES.containsKey(s)) {
            skipChars(i);
            return s;
        } else {
            return null;
        }
    }

    private String testOperation() {
        skipWhitespaces();
        if (Character.isLetter(ch)) {
            return testWordOperation();
        }
        for (int i = BUFFER_SIZE; i > 0; i--) {
            String s = String.valueOf(buffer, 0, i);
            if (PRIORITIES.containsKey(s)) {
                skipChars(i);
                return s;
            }
        }
        return null;
    }

    private void skipWhitespaces() {
        while (hasNext() && Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    private CommonExpression<T> parseOperation(int priority) throws ParserException {
        skipWhitespaces();
        if (priority == LAST_PRIORITY) {
            return parseUnary();
        }
        CommonExpression<T> result = parseOperation(priority + 1);
        skipWhitespaces();
        while (hasNext() || !last.isEmpty()) {
            String operator;
            if (last.isEmpty()) {
                operator = testOperation();
                skipWhitespaces();
            } else {
                operator = last;
                last = "";
            }
            if (operator == null) {
                break;
            }
            if (PRIORITIES.get(operator) != priority) {
                last = operator;
                break;
            }
            switch (operator) {
                case "+":
                    result = new Add<>(result, parseOperation(priority + 1));
                    break;
                case "-":
                    result = new Subtract<>(result, parseOperation(priority + 1));
                    break;
                case "*":
                    result = new Multiply<>(result, parseOperation(priority + 1));
                    break;
                case "/":
                    result = new Divide<>(result, parseOperation(priority + 1));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + operator);
            }
        }

        skipWhitespaces();
        return result;
    }

    private CommonExpression<T> parseUnary() throws ParserException {
        skipWhitespaces();
        String operator = testOperation();
        if (Objects.equals(operator, "-")) {
            skipWhitespaces();
            if (Character.isDigit(ch)) {
                return parseNumber(true);
            } else {
                return new Negative<>(parseOperation(LAST_PRIORITY));
            }
        } else if (operator != null) {
            throw new NoArgumentException(getPos() + ": " + "expected number or expression");
        }
        skipWhitespaces();
        if (test('(')) {
            CommonExpression<T> result = parseOperation(START_PRIORITY);
            expect(')');
            return result;
        }
        return parseSimpleElement();
    }

    private CommonExpression<T> parseSimpleElement() throws ParserException {
        skipWhitespaces();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (hasNext() && Character.isLetter(ch) || ch == '_' || count > 0 && Character.isDigit(ch)) {
            count++;
            sb.append(ch);
            nextChar();
        }
        if (count != 0) {
            try {
                return new Variable<>(sb.toString());
            } catch (ExpressionException e) {
                throw new ParserArithmeticException(getPos() + ": " + e.getMessage());
            }
        } else {
            return parseNumber(false);
        }
    }

    private CommonExpression<T> parseNumber(boolean negative) throws ParserException {
        StringBuilder sb = new StringBuilder();
        if (negative) {
            sb.append('-');
        }
        skipWhitespaces();
        while (hasNext() && Character.isDigit(ch)) {
            sb.append(ch);
            nextChar();
        }
        if (sb.length() == 0) {
            if (hasNext() && ch != ')') {
                throw new UnexpectedSymbolException(getPos() + ": " + ch);
            } else {
                throw new NoArgumentException(getPos() + ": " + "expected number or expression");
            }
        }
        try {
            return constParser.parse(sb.toString());
        } catch (NumberFormatException e) {
            throw new ParserArithmeticException(getPos() + ": overflow constant " + e.getMessage());
        }
    }
}
