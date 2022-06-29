package queue;

public class ArrayQueueModuleTest {
    private static void fill() {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue("e" + i);
        }
    }

    private static void dump() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current size: " + ArrayQueueModule.size());
            System.out.println("Current first element: " + ArrayQueueModule.element());
            System.out.println("Deleted this element: " + ArrayQueueModule.dequeue());
            System.out.println("Checking emptiness: " + ArrayQueueModule.isEmpty());
        }
    }

    private static void makeEmpty() {
        System.out.println("Before making empty size was " + ArrayQueueModule.size());
        ArrayQueueModule.clear();
        System.out.println("After making empty size was " + ArrayQueueModule.size());
    }

    private static void fillDeque() {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.push("E" + i);
        }
    }

    private static void dumpDeque() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Current last element: " + ArrayQueueModule.peek());
            System.out.println("Deleted this element: " + ArrayQueueModule.remove());
        }
    }

    private static void checkIndexes() {
        fill();
        System.out.println(ArrayQueueModule.indexOf("e0"));
        System.out.println(ArrayQueueModule.indexOf("e4"));
        System.out.println(ArrayQueueModule.indexOf("e5"));
        System.out.println(ArrayQueueModule.lastIndexOf("e0"));
        System.out.println(ArrayQueueModule.lastIndexOf("e4"));
        System.out.println(ArrayQueueModule.lastIndexOf("e5"));
    }

    public static void main(String[] args) {
        fill();
        dump();
        fill();
        makeEmpty();
        fillDeque();
        dumpDeque();
        checkIndexes();
    }
}
