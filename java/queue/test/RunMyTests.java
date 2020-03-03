package queue.test;

public class RunMyTests {
    public static void main(String[] args) {
        MyArrayQueueTest t3 = new MyArrayQueueTest();
        t3.test(1_000);
        System.out.println("t3 complete");
    }
}
