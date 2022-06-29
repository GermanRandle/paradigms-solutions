package queue;

public class ArrayQueueMyTest {
    private static void fill(ArrayQueue queue, int start) {
        for (int i = 0; i < 5; i++) {
            queue.enqueue("e" + (i + start));
        }
    }

    private static void dump(ArrayQueue queue) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current size: " + queue.size());
            System.out.println("Current first element: " + queue.element());
            System.out.println("Deleted this element: " + queue.dequeue());
            System.out.println("Checking emptiness: " + queue.isEmpty());
        }
    }

    private static void makeEmpty(ArrayQueue queue) {
        System.out.println("Before making empty size was " + queue.size());
        queue.clear();
        System.out.println("After making empty size was " + queue.size());
    }

    private static void fillDeque(ArrayQueue queue, int start) {
        for (int i = 0; i < 5; i++) {
            queue.push("E" + (i + start));
        }
    }

    private static void dumpDeque(ArrayQueue queue) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current last element: " + queue.peek());
            System.out.println("Deleted this element: " + queue.remove());
        }
    }

    private static void checkIndexes(ArrayQueue queue) {
        fill(queue, 0);
        System.out.println(queue.indexOf("e0"));
        System.out.println(queue.indexOf("e4"));
        System.out.println(queue.indexOf("e5"));
        System.out.println(queue.lastIndexOf("e0"));
        System.out.println(queue.lastIndexOf("e4"));
        System.out.println(queue.lastIndexOf("e5"));
    }

    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        fill(queue1, 0);
        dump(queue1);
        fill(queue2, 10);
        dump(queue2);
        makeEmpty(queue2);
        fillDeque(queue1, 0);
        fillDeque(queue2, 10);
        dumpDeque(queue1);
        dumpDeque(queue2);
        checkIndexes(queue1);
        checkIndexes(queue2);
    }
}
