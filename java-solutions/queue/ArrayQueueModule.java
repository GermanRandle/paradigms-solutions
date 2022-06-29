package queue;
// Model: a[1]..a[n]
// Invariant: n >= 0 && for i=1..n  a[i] != null

// Designations and definitions:
// 1) Let x be a variable in some method. Then x is a value at the start of a method, x' is a runtime value.
// SPECIAL: If x is some array, then x' == x means (for i = 0..x.length  x'[i] = x[i])

public class ArrayQueueModule {
    private static final int INITIAL_CAPACITY = 2;
    private static int head = 0;
    private static int tail = 0;
    private static Object[] elements = new Object[INITIAL_CAPACITY];

    // Precondition: true
    // Postcondition: R == n && n == n' && a == a'
    public static int size() {
        if (tail >= head) {
            return tail - head;
        }
        return elements.length - head + tail;
    }

    // Precondition: true
    // Postcondition: R == (n == 0) && n == n' && a == a'
    public static boolean isEmpty() {
        return size() == 0;
    }


    // Precondition: true
    // Postcondition: n' == 0
    public static void clear() {
        for (int i = head; i != tail; i = moduloInc(i)) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
    }

    // Precondition: n > 0
    // Postcondtion: R == a[1] && n' == n && a' == a
    public static Object element() {
        assert !isEmpty();
        return elements[head];
    }

    // Precondition: element != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i] == a[i] && a[n'] == element
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity(size() + 2);
        elements[tail] = element;
        tail = moduloInc(tail);
    }

    // Precondition: n > 0
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i + 1]
    public static Object dequeue() {
        assert !isEmpty();
        Object result = elements[head];
        elements[head] = null;
        head = moduloInc(head);
        return result;
    }

    // Precondition: element != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i + 1] == a[i] && a[1] == element
    public static void push(Object element) {
        assert element != null;
        ensureCapacity(size() + 2);
        head = moduloDec(head);
        elements[head] = element;
    }

    // Precondition: n > 0
    // Postcondtion: R == a[n] && n' == n && a' == a
    public static Object peek() {
        assert !isEmpty();
        return elements[moduloDec(tail)];
    }

    // Precondition: n > 0
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i]
    public static Object remove() {
        assert !isEmpty();
        tail = moduloDec(tail);
        Object result = elements[tail];
        elements[tail] = null;
        return result;
    }

    // Precondition: true
    // Postcondition: n' == n && a' == a && R == such i: 1 <= i <= n && a[i].equals(element) &&
    // for j = 1..(i - 1)  !a[j].equals(element). If such i does not exist, R == -1.
    public static int indexOf(Object element) {
        int curIndex = 0;
        for (int i = head; i != tail; i = moduloInc(i)) {
            if (elements[i].equals(element)) {
                return curIndex;
            }
            curIndex++;
        }
        return -1;
    }

    // Precondition: true
    // Postcondition: n' == n && a' == a && R == such i: 1 <= i <= n && a[i].equals(element) &&
    // for j = (i + 1)..n  !a[j].equals(element). If such i does not exist, R == -1.
    public static int lastIndexOf(Object element) {
        int curIndex = size() - 1;
        for (int i = moduloDec(tail); i != moduloDec(head); i = moduloDec(i)) {
            if (elements[i].equals(element)) {
                return curIndex;
            }
            curIndex--;
        }
        return -1;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] elementsExt = new Object[Math.max(capacity, 2 * elements.length)];
            for (int i = head; i != tail; i = moduloInc(i)) {
                if (i >= head) {
                    elementsExt[i] = elements[i];
                } else {
                    elementsExt[i + elements.length] = elements[i];
                }
            }
            if (tail < head) {
                tail += elements.length;
            }
            elements = elementsExt;
        }
    }

    private static int moduloDec(int x) {
        if (x > 0) {
            return x - 1;
        }
        return elements.length - 1;
    }

    private static int moduloInc(int x) {
        if (x < elements.length - 1) {
            return x + 1;
        }
        return 0;
    }
}
