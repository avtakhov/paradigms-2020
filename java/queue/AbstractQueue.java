package queue;

public abstract class AbstractQueue implements Queue {
    int size = 0;
    Node front, end;

    protected AbstractQueue() {
    }

    protected abstract Node next(Node t);

    protected abstract Object get(Node t);

    protected abstract void append(Object obj);

    @Override
    public void enqueue(Object obj) {
        append(obj);
        size++;
    }

    @Override
    public Object dequeue() {
        size--;
        Object ans = get(front);
        front = next(front);
        return ans;
    }

    @Override
    public Object element() {
        return get(front);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toStr() {
        StringBuilder sb = new StringBuilder("[");
        Node t = front;
        for (int i = 0; i < size(); i++) {
            sb.append(get(t));
            t = next(t);
            if (i + 1 < size()) {
                sb.append(", ");
            }
        }
        return sb.append(']').toString();
    }

    protected abstract static class Node {
    }
}
