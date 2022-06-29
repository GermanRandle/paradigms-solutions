package queue;

public class ArrayQueueADTTest {
    private static void fill(ArrayQueueADT queue, int start) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue, "e" + (i + start));
        }
    }

    private static void dump(ArrayQueueADT queue) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current size: " + ArrayQueueADT.size(queue));
            System.out.println("Current first element: " + ArrayQueueADT.element(queue));
            System.out.println("Deleted this element: " + ArrayQueueADT.dequeue(queue));
            System.out.println("Checking emptiness: " + ArrayQueueADT.isEmpty(queue));
        }
    }

    private static void makeEmpty(ArrayQueueADT queue) {
        System.out.println("Before making empty size was " + ArrayQueueADT.size(queue));
        ArrayQueueADT.clear(queue);
        System.out.println("After making empty size was " + ArrayQueueADT.size(queue));
    }

    private static void fillDeque(ArrayQueueADT queue, int start) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.push(queue, "E" + (i + start));
        }
    }

    private static void dumpDeque(ArrayQueueADT queue) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current last element: " + ArrayQueueADT.peek(queue));
            System.out.println("Deleted this element: " + ArrayQueueADT.remove(queue));
        }
    }

    private static void checkIndexes(ArrayQueueADT queue) {
        fill(queue, 0);
        System.out.println(ArrayQueueADT.indexOf(queue, "e0"));
        System.out.println(ArrayQueueADT.indexOf(queue, "e4"));
        System.out.println(ArrayQueueADT.indexOf(queue, "e5"));
        System.out.println(ArrayQueueADT.lastIndexOf(queue, "e0"));
        System.out.println(ArrayQueueADT.lastIndexOf(queue, "e4"));
        System.out.println(ArrayQueueADT.lastIndexOf(queue, "e5"));
    }

    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        fill(queue1, 0);
        dump(queue1);
        fill(queue2, 10);
        dump(queue2);
        makeEmpty(queue2);
        fillDeque(queue1, 0);
        dumpDeque(queue1);
        fillDeque(queue2, 10);
        dumpDeque(queue2);
        checkIndexes(queue1);
        checkIndexes(queue2);
    }
}
