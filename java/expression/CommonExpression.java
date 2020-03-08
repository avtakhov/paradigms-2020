package expression;

public interface CommonExpression<T> extends TripleExpression<T>, Expression<T> {

    int getPriorityLevel();

    String getOperation();

    default boolean isSpecialPhrase() {
        return false;
    }
}
