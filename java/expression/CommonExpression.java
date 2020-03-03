package expression;

public interface CommonExpression extends TripleExpression, Expression {

    int getPriorityLevel();

    String getOperation();

    default boolean isSpecialPhrase() {
        return false;
    }
}
