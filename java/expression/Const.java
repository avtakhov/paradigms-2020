package expression;

public class Const implements CommonExpression {
    int c;

    public Const(int c) {
        this.c = c;
    }

    public String toString() {
        return String.valueOf(c);
    }

    public String toMiniString() {
        return toString();
    }

    @Override
    public int getPriorityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getOperation() {
        return null;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return c;
    }

    @Override
    public int evaluate(int x) {
        return c;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Const constant = (Const) object;
        return this.c == constant.c;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(c);
    }
}
