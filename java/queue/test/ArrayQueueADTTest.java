package queue.test;

import queue.ArrayQueueADT;

public class ArrayQueueADTTest extends AbstractQueueIntegerTest {
    ArrayQueueADT q = new ArrayQueueADT();

    @Override
    void enqueue(Object t) {
        ArrayQueueADT.enqueue(q, t);
    }

    @Override
    Object dequeue() {
        return ArrayQueueADT.dequeue(q);
    }

    @Override
    Object element() {
        return ArrayQueueADT.element(q);
    }

    @Override
    void clear() {
        ArrayQueueADT.clear(q);
    }

    @Override
    boolean isEmpty() {
        return ArrayQueueADT.isEmpty(q);
    }

    @Override
    int size() {
        return ArrayQueueADT.size(q);
    }

    @Override
    String toStr() {
        return ArrayQueueADT.toStr(q);
    }
}
