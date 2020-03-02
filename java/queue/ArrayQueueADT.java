package queue;

public class ArrayQueueADT {
    // front - index of first element int queue
    // end - index of new element after enqueue()
    private Object[] elements = new Object[8];
    private int front = 0, end = 0;

    private static int inc(int x, int n) {
        return (x + 1) % n;
    }

    // PRE: q != null
    // POST: number of elements in queue
    public static int size(ArrayQueueADT q) {
        return (q.end - q.front + q.elements.length) % q.elements.length;
    }


    // PRE: q != null && this is first element of queue or Object type equals to prev element type
    // POST: push t at the end of queue
    public static void enqueue(ArrayQueueADT q, Object t) {
        checkCapacity(q, size(q) + 1);
        q.elements[q.end] = t;
        q.end = inc(q.end, q.elements.length);
    }

    // PRE: size > 0 && q != null
    // POST: returns front element by INV of front
    public static Object element(ArrayQueueADT q) {
        assert size(q) > 0;
        return q.elements[q.front];
    }


    // PRE: q != null && size > 0
    // POST: deletes and returns first element
    public static Object dequeue(ArrayQueueADT q) {
        assert size(q) > 0;
        Object ans = q.elements[q.front];
        q.front = inc(q.front, q.elements.length);
        return ans;
    }

    // PRE: q != null
    // POST: check if size equals to 0
    public static boolean isEmpty(ArrayQueueADT q) {
        return size(q) == 0;
    }

    // PRE: q != null
    // POST: clears queue
    public static void clear(ArrayQueueADT q) {
        q.elements = new Object[8];
        q.front = 0;
        q.end = 0;
    }

    // '[' + all values from front to end, divided by comma + ']'
    public static String toStr(ArrayQueueADT q) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = q.front; i != q.end; i = inc(i, q.elements.length)) {
            sb.append(q.elements[i]);
            if (inc(i, q.elements.length) == q.end) {
                return sb.append("]").toString();
            }
            sb.append(", ");
        }
        return sb.append("]").toString();
    }

    private static void checkCapacity(ArrayQueueADT q, int size) {
        if (size >= q.elements.length) {
            Object[] arr0 = new Object[q.elements.length * 2];
            for (int i = q.front, j = 0; i != q.end; i = inc(i, q.elements.length), j++) {
                arr0[j] = q.elements[i];
            }
            q.end = size(q);
            q.front = 0;
            q.elements = arr0;
        }
    }
}
