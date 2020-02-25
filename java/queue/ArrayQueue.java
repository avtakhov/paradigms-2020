package queue;

public class ArrayQueue {
    // front - index of first element int queue
    // end - index of new element after enqueue()
    private Object[] arr = new Object[8];
    private int l = 0, r = 0;

    // return index of next element after a[x]
    private int inc(int x) {
        return (x + 1) % arr.length;
    }

    // PRE: elements.length > size of queue
    // POST: size of queue
    public int size() {
        return (r - l + arr.length) % arr.length;
    }

    // PRE: this is first element of queue or Object type equals to prev element type
    // POST: pushes t at the end of queue
    public void enqueue(Object t) {
        checkCapacity(size() + 1);
        arr[r] = t;
        r = inc(r);
    }

    // PRE: size > 0
    // POST: returns front element by INV of front
    public Object element() {
        assert size() > 0;
        return arr[l];
    }

    // PRE: size > 0
    // POST: deletes and returns first element
    public Object dequeue() {
        assert size() > 0;
        Object ans = arr[l];
        l = inc(l);
        return ans;
    }

    // POST: check if size == 0
    public boolean isEmpty() {
        return size() == 0;
    }

    // POST: clears queue
    public void clear() {
        arr = new Object[8];
        l = 0;
        r = 0;
    }

    // PRE: size is expected size of queue after push request
    // POST: rebuild queue
    private void checkCapacity(int size) {
        if (size >= arr.length) {
            Object[] arr0 = new Object[arr.length * 2];
            for (int i = l, j = 0; i != r; i = inc(i), j++) {
                arr0[j] = arr[i];
            }
            r = size();
            l = 0;
            arr = arr0;
        }
    }
}
