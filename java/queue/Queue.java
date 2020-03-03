package queue;

public interface Queue {

    // add new element at the end of queue
    void enqueue(Object t);

    // PRE: size > 0
    // delete front element and return
    Object dequeue();

    // PRE: size > 0
    // returns front element from queue
    Object element();

    // clear queue
    void clear();

    // check size equals to 0
    boolean isEmpty();

    // number of queue elements
    int size();

    // '[' + all values from front to end, divided by comma + ']'
    default String toStr() {
        return null;
    }

    // returns an array containing all of the elements in this queue
    Object[] toArray();
}
