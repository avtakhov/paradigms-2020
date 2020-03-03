package expression.parser;

import expression.*;
import expression.parser.exceptions.*;

import java.util.Map;
import java.util.Objects;

public class ExpressionParser extends BaseParser implements Parser {
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

    @Override
    public expression.TripleExpression parse(String expression) throws ParserException {
        setSource(new StringSource(expression));
        TripleExpression res = parseOperation(START_PRIORITY);
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

    private CommonExpression parseOperation(int priority) throws ParserException {
        skipWhitespaces();
        if (priority == LAST_PRIORITY) {
            return parseUnary();
        }
        CommonExpression result = parseOperation(priority + 1);
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
                    result = new CheckedAdd(result, parseOperation(priority + 1));
                    break;
                case "-":
                    result = new CheckedSubtract(result, parseOperation(priority + 1));
                    break;
                case "*":
                    result = new CheckedMultiply(result, parseOperation(priority + 1));
                    break;
                case "/":
                    result = new CheckedDivide(result, parseOperation(priority + 1));
                    break;
            }
        }

        skipWhitespaces();
        return result;
    }

    private CommonExpression parseUnary() throws ParserException {
        skipWhitespaces();
        String operator = testOperation();
        if (Objects.equals(operator, "-")) {
            skipWhitespaces();
            if (Character.isDigit(ch)) {
                return parseNumber(true);
            } else {
                return new Negative(parseOperation(LAST_PRIORITY));
            }
        } else if (Objects.equals(operator, "log2")) {
            return new CheckedLog2(parseOperation(LAST_PRIORITY));
        } else if (Objects.equals(operator, "pow2")) {
            return new CheckedPow2(parseOperation(LAST_PRIORITY));
        } else if (operator != null) {
            throw new NoArgumentException(getPos() + ": " + "expected number or expression");
        }
        skipWhitespaces();
        if (test('(')) {
            CommonExpression result = parseOperation(START_PRIORITY);
            expect(')');
            return result;
        }
        return parseSimpleElement();
    }

    private CommonExpression parseSimpleElement() throws ParserException {
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
                return new Variable(sb.toString());
            } catch (ExpressionException e) {
                throw new ParserArithmeticException(getPos() + ": " + e.getMessage());
            }
        } else {
            return parseNumber(false);
        }
    }

    private CommonExpression parseNumber(boolean negative) throws ParserException {
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
            return new Const(Integer.parseInt(sb.toString()));
        } catch (NumberFormatException e) {
            throw new ParserArithmeticException(getPos() + ": overflow constant " + e.getMessage());
        }
    }
}
