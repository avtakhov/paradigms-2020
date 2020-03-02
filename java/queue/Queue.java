package queue;

public interface Queue {

    void enqueue(Object t);

    Object dequeue();

    Object element();

    void clear();

    boolean isEmpty();

    int size();

    default String toStr() {
        return null;
    }
}
