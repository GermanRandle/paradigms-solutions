package queue;
// Model: a[1]..a[n]
// Invariant: n >= 0 && for i=1..n  a[i] != null

// Designations and definitions:
// 1) Let x be a variable in some method. Then x is a value at the start of a method, x' is a runtime value.
// SPECIAL: If x is some array, then x' == x means (for i = 0..x.length  x'[i] = x[i])

public class ArrayQueueADT {
    private static final int INITIAL_CAPACITY = 2;
    private int head;
    private int tail;
    private Object[] elements;

    public ArrayQueueADT() {
        elements = new Object[INITIAL_CAPACITY];
    }

    // Precondition: queue != null
    // Postcondition: R == n && n == n' && a == a'
    public static int size(ArrayQueueADT queue) {
        if (queue.tail >= queue.head) {
            return queue.tail - queue.head;
        }
        return queue.elements.length - queue.head + queue.tail;
    }

    // Precondition: queue != null
    // Postcondition: R == (n == 0) && n == n' && a == a'
    public static boolean isEmpty(ArrayQueueADT queue) {
        return size(queue) == 0;
    }

    // Precondition: queue != null
    // Postcondition: n' == 0
    public static void clear(ArrayQueueADT queue) {
        for (int i = queue.head; i != queue.tail; i = moduloInc(queue, i)) {
            queue.elements[i] = null;
        }
        queue.head = 0;
        queue.tail = 0;
    }

    // Precondition: queue != null
    // Postcondtion: R == a[1] && n' == n && a' == a
    public static Object element(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        return queue.elements[queue.head];
    }

    // Precondition: element != null && queue != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i] == a[i] && a[n'] == element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue, size(queue) + 2);
        queue.elements[queue.tail] = element;
        queue.tail = moduloInc(queue, queue.tail);
    }

    // Precondition: n > 0 && queue != null
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = moduloInc(queue, queue.head);
        return result;
    }

    // Precondition: element != null && queue != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i + 1] == a[i] && a[1] == element
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue, size(queue) + 2);
        queue.head = moduloDec(queue, queue.head);
        queue.elements[queue.head] = element;
    }

    // Precondition: n > 0 && queue != null
    // Postcondtion: R == a[n] && n' == n && a' == a
    public static Object peek(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        return queue.elements[moduloDec(queue, queue.tail)];
    }

    // Precondition: n > 0 && queue != null
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i]
    public static Object remove(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        queue.tail = moduloDec(queue, queue.tail);
        Object result = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        return result;
    }

    // Precondition: queue != null
    // Postcondition: n' == n && a' == a && R == such i: 1 <= i <= n && a[i].equals(element) &&
    // for j = 1..(i - 1)  !a[j].equals(element). If such i does not exist, R == -1.
    public static int indexOf(ArrayQueueADT queue, Object element) {
        int curIndex = 0;
        for (int i = queue.head; i != queue.tail; i = moduloInc(queue, i)) {
            if (queue.elements[i].equals(element)) {
                return curIndex;
            }
            curIndex++;
        }
        return -1;
    }

    // Precondition: queue != null
    // Postcondition: n' == n && a' == a && R == such i: 1 <= i <= n && a[i].equals(element) &&
    // for j = (i + 1)..n  !a[j].equals(element). If such i does not exist, R == -1.
    public static int lastIndexOf(ArrayQueueADT queue, Object element) {
        int curIndex = size(queue) - 1;
        for (int i = moduloDec(queue, queue.tail); i != moduloDec(queue, queue.head); i = moduloDec(queue, i)) {
            if (queue.elements[i].equals(element)) {
                return curIndex;
            }
            curIndex--;
        }
        return -1;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            Object[] elementsExt = new Object[Math.max(capacity, 2 * queue.elements.length)];
            for (int i = queue.head; i != queue.tail; i = moduloInc(queue, i)) {
                if (i >= queue.head) {
                    elementsExt[i] = queue.elements[i];
                } else {
                    elementsExt[i + queue.elements.length] = queue.elements[i];
                }
            }
            if (queue.tail < queue.head) {
                queue.tail += queue.elements.length;
            }
            queue.elements = elementsExt;
        }
    }

    private static int moduloDec(ArrayQueueADT queue, int x) {
        if (x > 0) {
            return x - 1;
        }
        return queue.elements.length - 1;
    }

    private static int moduloInc(ArrayQueueADT queue, int x) {
        if (x < queue.elements.length - 1) {
            return x + 1;
        }
        return 0;
    }
}
