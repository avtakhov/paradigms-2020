package queue;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

public abstract class AbstractQueueIntegerTest {

    protected Random random = new Random();
    private Queue<Integer> q = new ArrayDeque<>();

    abstract void enqueue(Object t);

    abstract Object dequeue();

    abstract Object element();

    abstract void clear();

    abstract boolean isEmpty();

    abstract int size();

    Request nextRequest() {
        // return Request.DEQUEUE;
        return Request.values()[random.nextInt(Request.values().length)];
    }

    public void test(int count) {
        for (int i = 0; i < count; i++) {
            Request request = nextRequest();
            // System.out.println(request);
            switch (request) {
                case ENQUEUE:
                    int value = random.nextInt();
                    q.add(value);
                    enqueue(value);
                    break;
                case SIZE:
                    // System.out.println(size() + " " + q.size());
                    assert size() == q.size();
                    break;
                case DEQUEUE:
                    assert q.isEmpty() || Objects.equals(q.remove(), dequeue());
                    break;
                case CLEAR:
                    q.clear();
                    clear();
                    break;
                case ELEMENT:
                    assert q.isEmpty() || Objects.equals(q.element(), element());
                    break;
                case IS_EMPTY:
                    assert isEmpty() == q.isEmpty();
                    break;
            }
        }
    }

    enum Request {
        ENQUEUE, DEQUEUE, ELEMENT, CLEAR, IS_EMPTY, SIZE
    }

}
