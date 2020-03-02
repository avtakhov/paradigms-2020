package queue.test;

import queue.ArrayQueue;
import queue.Queue;

public class MyArrayQueueTest extends AbstractQueueIntegerTest {
    Queue q = new ArrayQueue();

    @Override
    void enqueue(Object t) {
        q.enqueue(t);
    }

    @Override
    Object dequeue() {
        return q.dequeue();
    }

    @Override
    Object element() {
        return q.element();
    }

    @Override
    void clear() {
        q.clear();
    }

    @Override
    boolean isEmpty() {
        return q.isEmpty();
    }

    @Override
    int size() {
        return q.size();
    }

    @Override
    String toStr() {
        return q.toStr();
    }


}
