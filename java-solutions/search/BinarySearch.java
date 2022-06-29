package search;

// Designations and definitions:
// 1) $x$ can be used to differ mathematical expression from text.
// 2) Let $x$ be a variable in some method. Then $x$ is a value at the start of a method, $x'$ is a runtime value.
// SPECIAL: If $x$ is some array, then $x' == x$ means (for i = 0..x.length  x'[i] = x[i])
// 3) If $a$ is an array, then a[l:r], a[l:], a[:r] are equivalent to Python slices.
// 4) "Non-increasing array" == some array $a$: for i=0..a.length - 2: a[i] >= a[i + 1]
// 5) "The answer" == if $a$ is a non-decreasing array, then it is such i: 0 <= i <= a.length, a[i] <= $x$,
// for all j: 0 < j < i  a[j] > $x$
// 6) If $a$ is a non-decreasing array, then a[-1] == +inf, a[a.length] == -inf in this code.
// 7) "The statement" == $x$ is some number, $a$ is some non-descending array.

public class BinarySearch {
    // Precondition: $a$ != null, $a$ is non-increasing, -1 <= l < r <= a.length,  a[l] > x >= a[r]
    public static int binSearchRecursive(int[] a, int x, int l, int r) {
        // -1 <= l < r <= a.length, a[l] > x >= a[r], a' = a
        if (r - l == 1) {
            // -1 <= l < r <= a.length, a[l] > x >= a[l + 1], a' = a
            // for all j: 0 < j < i  a[j] > x because a is non-increasing
            // a[l + 1] <= x, thus, (l + 1) == r is the answer for $a$
            return r;
        }
        // -1 <= l < r <= a.length, a[l] > x >= a[r], r - l > 1, a' = a
        int mid = (l + r) / 2;
        // -1 <= l < mid < r <= a.length, a[l] > x >= a[r], a' = a
        if (a[mid] <= x) {
            // -1 <= l < mid < r <= a.length, a[l] > x >= a[mid] >= a[r], a' = a
            // Checking precondition:
            // $a$ != null, $a$ is non-increasing, -1 <= l < mid <= a.length, a[l] > x >= a[mid]
            return binSearchRecursive(a, x, l, mid);
            // returns the answer for $a$.
        }
        // -1 <= l < mid < r <= a.length, a[l] > x >= a[r], a[mid] > x, a' = a
        // Checking precondition:
        // $a$ != null, $a$ is non-increasing, -1 <= mid < r <= a.length, a[mid] > x >= a[r]
        return binSearchRecursive(a, x, mid, r);
        // returns the answer for $a$.
    }
    // Postcondition: returns the answer for $a$, $x$.

    // Precondition: $a$ != null && $a$ is non-increasing
    public static int binSearchIterative(int[] a, int x) {
        // a' = a
        int l = -1, r = a.length;
        // Loop invariant: a[l] > x >= a[r], a' = a
        while (r - l > 1) {
            // a[l] > x >= a[r], a' = a, r - l > 1
            int mid = (l + r) / 2;
            // a[l] > x >= a[r], a' = a, l < mid < r
            if (a[mid] <= x) {
                // a[l] > x => a[mid] >= a[r], a' = a, l < mid < r
                r = mid;
                // a[l] > x => a[r], a' = a
            } else {
                // $a[l] >= a[mid] > x >= a[r], a' = a, l < mid < r
                l = mid;
                // a[mid] > x >= a[r], a' = a
            }
            // a[l] > x >= a[r], a' = a, hence, invariant saved
        }
        // a[l] > x >= a[l + 1], a' = a
        // for all j: 0 < j < i  a[j] > x because a is non-increasing
        // a[l + 1] <= x, thus, (l + 1) == r is the answer for $a$
        return r;
    }
    // Postcondition: returns "the answer" for $a$, $x$

    // Precondition: args has at least 1 element, args contain only string representations of integers in range
    // [-2^31:2^31-1], args[1:] is non-increasing
    public static void main(String[] args) {
        // args' == args
        int bound = Integer.parseInt(args[0]);
        // bound = $x$ from the statement, args == args'
        int[] array = new int[args.length - 1];
        // array.length == a.length, for all j = 0.. a.length - 1  array[j] == 0, $bound$ = $x$ from the
        // statement, args' == args
        for (int i = 0; i < args.length - 1; i++) {
            // array[:i] == a[:i] (a -- from the statement), for all j: j = i..a.length - 1  a[j] == 0,
            // args' == args, $bound$ = $x$ from the statement, args' == args
            array[i] = Integer.parseInt(args[i + 1]);
            // array[:i + 1] == a[:i + 1] (a -- from the statement), for all j = i + 1..a.length - 1  a[j] == 0,
            // args' == args, $bound$ = $x$ from the statement, args' == args
        }
        // array == $a$ from the statement, $bound$ = $x$ from the statement.
        int resIterative = binSearchIterative(array, bound);
        // resIterative == "the answer", array' = array
        int resRecursive = binSearchRecursive(array, bound, -1, array.length);
        // resRecursive == "the answer". Thus, resIterative == resRecursive == "the answer"
        if (resIterative == resRecursive) {
            // resIterative == resRecursive == "the answer" (always reached)
            System.out.println(resIterative);
            // printed "the answer" for $x$ = $args[0]$ and $a$ = $args[1:]$ from the statement
            // on the standard output stream (System.out).
        } else {
            // false (cannot be reached)
            System.out.println("It's a bug!");
        }
        // printed "the answer" for $x$ = $args[0]$ and $a$ = $args[1:]$ from the statement
        // on the standard output stream (System.out).
    }
    // Postcondition: printed "the answer" for $x$ = $args[0]$ and $a$ = $args[1:]$ from the statement
    // on the standard output stream (System.out).
}

