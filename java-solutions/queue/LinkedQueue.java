package queue;

import java.util.ArrayList;
import java.util.function.Predicate;

// Model: a[1]..a[n]
// Invariant: n >= 0 && for i=1..n  a[i] != null

// Designations and definitions:
// 1) Let x be a variable in some method. Then x is a value at the start of a method, x' is a runtime value.
// SPECIAL: If x is some array, then x' == x means (for i = 0..x.length  x'[i] = x[i])

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private final Object element;
        private Node next;

        public Node(Object element) {
            this.element = element;
            this.next = null;
        }
    }

    private int size;
    private Node head = null;
    private Node tail = null;

    // Precondition: true
    // Postcondition: R == n && n == n' && a == a'
    public int size() {
        return size;
    }

    // Precondition: true
    // Postcondition: n' == 0
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // Precondition: n > 0
    // Postcondtion: R == a[1] && n' == n && a' == a
    public Object elementImpl() {
        return head.element;
    }

    // Precondition: element != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i] == a[i] && a[n'] == element
    public void enqueueImpl(Object element) {
        Node newNode = new Node(element);
        if (size() == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Precondition: n > 0
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i + 1] && R == a[1]
    public Object dequeueImpl() {
        Object result = head.element;
        head = head.next;
        size--;
        return result;
    }
}
