package expression;

import expression.parser.exceptions.ExpressionException;

public class Variable implements CommonExpression {

    String s;

    public Variable(String s) {
        if (!s.equals("x") && !s.equals("y") && !s.equals("z")) {
            throw new ExpressionException("Invalid variable name " + s);
        }
        this.s = s;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int getPriorityLevel() {
        return 10;
    }

    @Override
    public String getOperation() {
        return null;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (s) {
            case "x":
                return x;
            case "y":
                return y;
            default:
                return z;
        }
    }


    @Override
    public int evaluate(int x) {
        return x;
    }

    public String toString() {
        return s;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Variable variable = (Variable) object;
        return this.s.equals(variable.s);
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }

}
