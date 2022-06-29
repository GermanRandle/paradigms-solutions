package queue;

public class LinkedQueueTest {
    private static void fill(LinkedQueue queue, int start) {
        for (int i = 0; i < 5; i++) {
            queue.enqueue("e" + (i + start));
        }
    }

    private static void dump(LinkedQueue queue) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current size: " + queue.size());
            System.out.println("Current first element: " + queue.element());
            System.out.println("Deleted this element: " + queue.dequeue());
            System.out.println("Checking emptiness: " + queue.isEmpty());
        }
    }

    private static void makeEmpty(LinkedQueue queue) {
        System.out.println("Before making empty size was " + queue.size());
        queue.clear();
        System.out.println("After making empty size was " + queue.size());
    }

    public static void main(String[] args) {
        LinkedQueue queue1 = new LinkedQueue();
        LinkedQueue queue2 = new LinkedQueue();
        fill(queue1, 0);
        dump(queue1);
        fill(queue2, 10);
        dump(queue2);
        makeEmpty(queue2);
    }
}
