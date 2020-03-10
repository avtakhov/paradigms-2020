package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private final static int START_CAPACITY = 8;
    private Object[] elements = new Object[START_CAPACITY];

    public ArrayQueue() {
        front = new Index(0);
        end = new Index(0);
    }

    private int inc(int x) {
        return (x + 1) % elements.length;
    }

    // POST: clears queue
    public void clear() {
        elements = new Object[START_CAPACITY];
        front = new Index(0);
        end = new Index(0);
        super.size = 0;
    }

    private void checkCapacity() {
        if (size() >= elements.length) {
            elements = Arrays.copyOf(toArray(), elements.length * 2);
            /*
            Object[] arr0 = new Object[elements.length * 2];
            Node v = front;
            for (int j = 0; j < size(); j++) {
                arr0[j] = get(v);
                v = next(v);
            }
             */
            end = new Index(size());
            front = new Index(0);

        }
    }

    @Override
    protected Node next(Node t) {
        return new Index(inc(((Index) t).i));
    }

    @Override
    protected Object get(Node t) {
        return elements[((Index) t).i];
    }

    @Override
    protected void append(Object obj) {
        checkCapacity();
        elements[((Index) end).i] = obj;
        end = next(end);
    }

    static class Index extends Node {
        public int i;

        public Index(int i) {
            this.i = i;
        }
    }

}
