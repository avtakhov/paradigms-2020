package queue;

public class LinkedQueue extends AbstractQueue {

    public LinkedQueue() {
        front = null;
        end = null;
    }

    @Override
    protected Node next(Node t) {
        return ((ListNode) t).next;
    }

    @Override
    protected Object get(Node t) {
        return ((ListNode) t).value;
    }

    @Override
    protected void append(Object obj) {
        Node t = new ListNode(obj, null);
        if (front == null) {
            end = front = t;
        } else {
            ListNode listEnd = (ListNode) end;
            listEnd.next = t;
            end = t;
        }
    }

    @Override
    public void clear() {
        front = end = null;
        size = 0;
    }

    private class ListNode extends Node {
        private Object value;
        private Node next;

        ListNode(Object value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }
}
