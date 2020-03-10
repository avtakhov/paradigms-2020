package queue;

public class ArrayQueueModule {
    private static Object[] elements = new Object[8];
    private static int front = 0, end = 0;

    private static int inc(int x) {
        return (x + 1) % elements.length;
    }

    // PRE: elements.length > size of queue
    // POST: size of queue
    public static int size() {
        return (end - front + elements.length) % elements.length;
    }

    // PRE: this is first element of queue or Object type equals to prev element type
    // POST: pushes t at the end of queue
    public static void enqueue(Object t) {
        checkCapacity(size() + 1);
        elements[end] = t;
        // next element goes to end + 1
        end = inc(end);
    }

    // PRE: size > 0
    // POST: returns front element by INV of front
    public static Object element() {
        assert size() > 0;
        return elements[front];
    }

    // PRE: size > 0
    // POST: deletes and returns first element
    public static Object dequeue() {
        assert size() > 0;
        // front element
        Object ans = elements[front];
        // the next after front
        front = inc(front);
        return ans;
    }

    // POST: check if size equals to 0
    public static boolean isEmpty() {
        return size() == 0;
    }

    // POST: clears queue
    public static void clear() {
        elements = new Object[8];
        front = 0;
        end = 0;
    }

    // '[' + all values from front to end, divided by comma + ']'
    public static String toStr() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = front; i != end; i = inc(i)) {
            sb.append(elements[i]);
            if (inc(i) == end) {
                return sb.append("]").toString();
            }
            sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // PRE: size is expected size of queue after push request
    // POST: rebuild queue
    private static void checkCapacity(int size) {
        if (size >= elements.length) {
            Object[] arr0 = new Object[elements.length * 2];
            if (front <= end) {
                System.arraycopy(elements, front, arr0, 0, size());
            } else {
                System.arraycopy(elements, front, arr0, 0, elements.length - front);
                System.arraycopy(elements, 0, arr0, elements.length - front, end);
            }
            end = size();
            front = 0;
            elements = arr0;
        }
    }

}
