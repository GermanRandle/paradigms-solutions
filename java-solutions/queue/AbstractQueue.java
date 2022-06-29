package queue;

import java.util.ArrayList;
import java.util.function.Predicate;

// Model: a[1]..a[n]
// Invariant: n >= 0 && for i=1..n  a[i] != null

public abstract class AbstractQueue implements Queue {
    // Precondition: true
    // Postcondition: R == (n == 0) && n == n' && a == a'
    public boolean isEmpty() {
        return size() == 0;
    }

    // Precondition: n > 0
    // Postcondtion: R == a[1] && n' == n && a' == a
    public Object element() {
        assert !isEmpty();
        return elementImpl();
    }

    // Precondition: n > 0
    // Postcondtion: R == a[1] && n' == n && a' == a
    protected abstract Object elementImpl();

    // Precondition: element != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i] == a[i] && a[n'] == element
    public void enqueue(Object element) {
        assert element != null;
        enqueueImpl(element);
    }

    // Precondition: element != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i] == a[i] && a[n'] == element
    protected abstract void enqueueImpl(Object element);

    // Precondition: n > 0
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i + 1] && R == a[1]
    public Object dequeue() {
        assert !isEmpty();
        return dequeueImpl();
    }

    // Precondition: n > 0
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i + 1] && R == a[1]
    protected abstract Object dequeueImpl();

    // Precondition: true
    // Postcondition: for i = 1..n'  predicate.test(a'[i]) == false && exist such i_1..i_n':
    // 1 <= i_1 < i_2 < ... < i_n' <= n && for j = 1..n'  a'[j] == a[i_j]
    // Let k_1 .. k_(n - n')  =  [1:n] \ {i_1 .. i_n}, k_1 < k_2 < ... < k_(n - n')
    // for j = 1..(n - n') predicate.test(a[k_j]) == true
    public void removeIf(Predicate<Object> predicate) {
        retainIf(predicate.negate());
    }

    private void retainGood(Predicate<Object> predicate, boolean breakAfterBad) {
        ArrayList<Object> survivors = new ArrayList<>();
        while (!isEmpty()) {
            if (predicate.test(element())) {
                survivors.add(element());
            } else if (breakAfterBad) {
                clear();
                break;
            }
            dequeue();
        }
        for (Object element : survivors) {
            enqueue(element);
        }
    }

    // Precondition: true
    // Postcondition: for i = 1..n'  predicate.test(a'[i]) == true && exist such i_1..i_n':
    // 1 <= i_1 < i_2 < ... < i_n' <= n && for j = 1..n'  a'[j] == a[i_j]
    // Let k_1 .. k_(n - n')  =  [1:n] \ {i_1 .. i_n}, k_1 < k_2 < ... < k_(n - n')
    // for j = 1..(n - n') predicate.test(a[k_j]) == false
    public void retainIf(Predicate<Object> predicate) {
        retainGood(predicate, false);
    }

    // Precondition: true
    // Postcondition: for i = 1..n' ( predicate.test(a'[i]) == true && a'[i] == a[i] ) &&
    // (n == n' || predicate.test(a[n' + 1]) == false)
    public void takeWhile(Predicate<Object> predicate) {
        retainGood(predicate, true);
    }

    // Precondition: true
    // Postcondition: for i = (n - n' + 1)..n  a[i] == a'[i - (n - n')] &&
    // for i = 1..(n - n')  predicate.test(a[i]) == true && (n' == 0 || predicate.test(a[n - n' + 1] == false)
    public void dropWhile(Predicate<Object> predicate) {
        while (!isEmpty() && predicate.test(element())) {
            dequeue();
        }
    }
}
