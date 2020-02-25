package queue;

public class ArrayQueueADT {
    // front - index of first element int queue
    // end - index of new element after enqueue()
    private Object[] arr = new Object[8];
    private int l = 0, r = 0;

    // return index of next element after a[x]
    private static int inc(int x, int n) {
        return (x + 1) % n;
    }

    // PRE: elements.length > size of queue
    // POST: size of queue
    public static int size(ArrayQueueADT q) {
        return (q.r - q.l + q.arr.length) % q.arr.length;
    }

    // PRE: this is first element of queue or Object type equals to prev element type
    // POST: pushes t at the end of queue
    public static void enqueue(ArrayQueueADT q, Object t) {
        checkCapacity(q, size(q) + 1);
        q.arr[q.r] = t;
        q.r = inc(q.r, q.arr.length);
    }

    // PRE: size > 0
    // POST: returns front element by INV of front
    public static Object element(ArrayQueueADT q) {
        assert size(q) > 0;
        return q.arr[q.l];
    }

    // PRE: size > 0
    // POST: deletes and returns first element
    public static Object dequeue(ArrayQueueADT q) {
        assert size(q) > 0;
        Object ans = q.arr[q.l];
        q.l = inc(q.l, q.arr.length);
        return ans;
    }

    // POST: check if size == 0
    public static boolean isEmpty(ArrayQueueADT q) {
        return size(q) == 0;
    }

    // POST: clears queue
    public static void clear(ArrayQueueADT q) {
        q.arr = new Object[8];
        q.l = 0;
        q.r = 0;
    }

    // PRE: size is expected size of queue after push request
    // POST: rebuild queue
    private static void checkCapacity(ArrayQueueADT q, int size) {
        if (size >= q.arr.length) {
            Object[] arr0 = new Object[q.arr.length * 2];
            for (int i = q.l, j = 0; i != q.r; i = inc(i, q.arr.length), j++) {
                arr0[j] = q.arr[i];
            }
            q.r = size(q);
            q.l = 0;
            q.arr = arr0;
        }
    }
}
