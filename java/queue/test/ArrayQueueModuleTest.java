package queue.test;

import queue.ArrayQueueModule;

public class ArrayQueueModuleTest extends AbstractQueueIntegerTest {

    @Override
    void enqueue(Object t) {
        ArrayQueueModule.enqueue(t);
    }

    @Override
    Object dequeue() {
        return ArrayQueueModule.dequeue();
    }

    @Override
    Object element() {
        return ArrayQueueModule.element();
    }

    @Override
    void clear() {
        ArrayQueueModule.clear();
    }

    @Override
    boolean isEmpty() {
        return ArrayQueueModule.isEmpty();
    }

    @Override
    int size() {
        return ArrayQueueModule.size();
    }

    @Override
    String toStr() {
        return ArrayQueueModule.toStr();
    }


}
