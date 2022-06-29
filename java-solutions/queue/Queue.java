package queue;

import java.util.function.Predicate;

// Model: a[1]..a[n]
// Invariant: n >= 0 && for i=1..n  a[i] != null

// Designations and definitions:
// 1) Let x be a variable in some method. Then x is a value at the start of a method, x' is a runtime value.
// SPECIAL: If x is some array, then x' == x means (for i = 0..x.length  x'[i] = x[i])

public interface Queue {
    // Precondition: true
    // Postcondition: R == n && n == n' && a == a'
    int size();

    // Precondition: true
    // Postcondition: R == (n == 0) && n == n' && a == a'
    boolean isEmpty();

    // Precondition: true
    // Postcondition: n' == 0
    void clear();

    // Precondition: n > 0
    // Postcondtion: R == a[1] && n' == n && a' == a
    Object element();

    // Precondition: element != null
    // Postcondition: n' == n + 1 && for i = 1..n  a'[i] == a[i] && a[n'] == element
    void enqueue(Object element);

    // Precondition: n > 0
    // Postcondition: n' == n - 1 && for i = 1..(n - 1)  a'[i] == a[i + 1]
    Object dequeue();

    // Precondition: true
    // Postcondition: for i = 1..n'  predicate.test(a'[i]) == false && exist such i_1..i_n':
    // 1 <= i_1 < i_2 < ... < i_n' <= n && for j = 1..n'  a'[j] == a[i_j]
    // Let k_1 .. k_(n - n')  =  [1:n] \ {i_1 .. i_n}, k_1 < k_2 < ... < k_(n - n')
    // for j = 1..(n - n') predicate.test(a[k_j]) == true
    void removeIf(Predicate<Object> predicate);

    // Precondition: true
    // Postcondition: for i = 1..n'  predicate.test(a'[i]) == true && exist such i_1..i_n':
    // 1 <= i_1 < i_2 < ... < i_n' <= n && for j = 1..n'  a'[j] == a[i_j]
    // Let k_1 .. k_(n - n')  =  [1:n] \ {i_1 .. i_n}, k_1 < k_2 < ... < k_(n - n')
    // for j = 1..(n - n') predicate.test(a[k_j]) == false
    void retainIf(Predicate<Object> predicate);

    // Precondition: true
    // Postcondition: for i = 1..n' ( predicate.test(a'[i]) == true && a'[i] == a[i] ) &&
    // (n == n' || predicate.test(a[n' + 1]) == false)
    void takeWhile(Predicate<Object> predicate);

    // Precondition: true
    // Postcondition: for i = (n - n' + 1)..n  a[i] == a'[i - (n - n')] &&
    // for i = 1..(n - n')  predicate.test(a[i]) == true && (n' == 0 || predicate.test(a[n - n' + 1] == false)
    void dropWhile(Predicate<Object> predicate);
}
