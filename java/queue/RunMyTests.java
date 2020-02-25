package queue;

public class RunMyTests {
    public static void main(String[] args) {
        ArrayQueueModuleTest t1 = new ArrayQueueModuleTest();
        t1.test(1_000_000);
        System.out.println("t1 complete");
        ArrayQueueADTTest t2 = new ArrayQueueADTTest();
        t2.test(1_000_000);
        System.out.println("t2 complete");
        MyArrayQueueTest t3 = new MyArrayQueueTest();
        t3.test(1_000_000);
        System.out.println("t3 complete");
    }
}
