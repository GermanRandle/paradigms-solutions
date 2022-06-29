package search;

// Designations and definitions:
// 1) $x$ can be used to differ mathematical expression from text.
// 2) Let $x$ be a variable in some method. Then $x$ is a value at the start of a method, $x'$ is a runtime value.
// SPECIAL: If $x$ is some array, then $x' == x$ means (for i = 0..x.length  x'[i] = x[i])
// 3) If $a$ is an array, then a[l:r], a[l:], a[:r] are equivalent to Python slices.
// 4) "Descending array" == some array $a$: for i=0..a.length - 2: a[i] > a[i + 1]
// 5) "Increasing array" == some array $a$: for i=0..a.length - 2: a[i] < a[i + 1]
// 6) "Glued array" == some array $a$: exists i: 0 <= i <= a.length, a[:i] is descending, a[i:] is increasing
// 7) "The answer" == if $a$ is a glued array, then it is such i: 0 <= i <= a.length, a[:i] is descending,
//     and a[i:] is increasing, for all j: 0 <= j < i  j is not the answer.
// 8) If $a$ is a glued array, then a[-1] == +inf, a[a.length] == +inf in this code.

public class BinarySearchUni {
    // Precondition: $a$ != null,  $a$ is a glued array, -1 <= l < r < a.length, a[r] < a[r + 1], a[l] >= a[l + 1]
    public static int binSearchRecursive(int[] a, int l, int r) {
        // -1 <= l < r < a.length, a[r] < a[r + 1], a[l] >= a[l + 1], a' = a
        if (r - l == 1) {
            // a[l] >= a[l + 1] < a[l + 2], a' = a
            // Also we know that $a$ is glued -> exists i: 0 <= i <= a.length, a[:i] is descending, a[i:] is increasing
            // a[l] >= a[l + 1], thus, i: 0 <= i <= l are not the answers (a[i:] is not increasing)
            // a[l + 1] < a[l + 2], thus, i: l + 3 <= i < a.length are not the answers (a[:i] is not descending).
            // Only two options left: (l + 1) and (l + 2).
            // But if a[:l + 2] is descending and a[l + 2:] is increasing, then a[:l + 1] is descending and a[l + 1:]
            // is increasing too. Thus, (l + 2) is not the answer, because condition "for all j: 0 <= j < i
            // j is not the answer" won't be met
            // One option left: (l + 1) == r -> this is the answer for $a$
            return r;
        }
        // -1 <= l < r < a.length, a[r] < a[r + 1], a[l] >= a[l + 1], r - l > 1, a'= a
        int mid = (l + r) / 2;
        // -1 <= l < mid < r < a.length, a[r] < a[r + 1], a[l] >= a[l + 1], a' = a
        if (a[mid] < a[mid + 1]) {
            // -1 <= l < mid < r < a.length, a[r] < a[r + 1], a[l] >= a[l + 1], a[mid] < a[mid + 1], a' = a
            // Check precondition:
            // $a$ != null,  $a$ is a glued array, -1 <= l < mid < a.length, a[mid] < a[mid + 1], a[l] >= a[l + 1]
            return binSearchRecursive(a, l, mid);
            // returns "the answer" for $a$.
        }
        // -1 <= l < mid < r < a.length, a[r] < a[r + 1], a[l] >= a[l + 1], a[mid] >= a[mid + 1], a' = a
        // Check precondition:
        // $a$ != null,  $a$ is a glued array, -1 <= mid < r < a.length, a[r] < a[r + 1], a[mid] >= a[mid + 1]
        return binSearchRecursive(a, mid, r);
        // returns "the answer" for $a$.
    }
    // Postcondition: returns "the answer" for $a$.

    // Precondition: $a$ != null, $a$ is a glued array
    public static int binSearchIterative(int[] a) {
        // a' = a
        int l = -1, r = a.length - 1;
        // Loop invariant: a[r] < a[r + 1], a[l] >= a[l + 1], a' = a
        while (r - l > 1) {
            // a[r] < a[r + 1], a[l] >= a[l + 1], a' = a, r - l > 1
            int mid = (l + r) / 2;
            // a[r] < a[r + 1], a[l] >= a[l + 1], a' = a, l < mid < r
            if (a[mid] < a[mid + 1]) {
                // a[r] < a[r + 1], a[mid] < a[mid + 1], a[l] >= a[l + 1], a' = a, l < mid < r
                r = mid;
                // a[r] < a[r + 1], a[l] >= a[l + 1], a' = a
            } else {
                // a[r] < a[r + 1], a[mid] >= a[mid + 1], a[l] >= a[l + 1], a' = a, l < mid < r
                l = mid;
                // a[r] < a[r + 1], a[l] >= a[l + 1], a' = a
            }
            // a[r] < a[r + 1], a[l] >= a[l + 1], a' = a -> loop invariant saved
        }
        // a[r] < a[r + 1], a[l] >= a[l + 1], a' = a, r == l + 1
        // Thus, a[l] >= a[l + 1] < a[l + 2]
        // Also we know that $a$ is glued -> exists i: 0 <= i <= a.length, a[:i] is descending, a[i:] is increasing
        // a[l] >= a[l + 1], thus, i: 0 <= i <= l are not the answers (a[i:] is not increasing)
        // a[l + 1] < a[l + 2], thus, i: l + 3 <= i < a.length are not the answers (a[:i] is not descending).
        // Only two options left: (l + 1) and (l + 2).
        // But if a[:l + 2] is descending and a[l + 2:] is increasing, then a[:l + 1] is descending and a[l + 1:]
        // is increasing too. Thus, (l + 2) is not the answer, because condition "for all j: 0 <= j < i
        // j is not the answer" won't be met
        // One option left: (l + 1) == r -> this is the answer
        return r;
    }
    // Postcondition: returns "the answer" for $a$.

    // Precondition: args has at least 1 element, args contain only string representations of integers in range
    // [-2^31:2^31-1], args is a glued array
    public static void main(String[] args) {
        // args' == args
        int[] a = new int[args.length];
        // a.length == args.length, for all j = 0..a.length - 1  a[j] == 0, args' == args
        for (int i = 0; i < args.length; i++) {
            // args[:i] == a[:i], for all j = 0..a.length - 1  a[j] == 0, args' == args
            a[i] = Integer.parseInt(args[i]);
            // args[:i + 1] == a[:i + 1], for all j = i + 1..a.length - 1  a[j] == 0, args' == args
        }
        // $a$ == args
        int resIterative = binSearchIterative(a);
        // $resIterative$ == "the answer", $a$ == args
        int resRecursive = binSearchRecursive(a, -1, args.length - 1);
        // resRecursive == "the answer". Thus, resIterative == resRecursive == "the answer"
        if (resIterative == resRecursive) {
            // resIterative == resRecursive == "the answer" (always reached)
            System.out.println(resIterative);
            // printed "the answer" for args on the standard output stream (System.out).
        } else {
            // false (cannot be reached)
            System.out.println("It's a bug!");
        }
        // printed "the answer" for args on the standard output stream (System.out).
    }
    // Postcondition: printed "the answer" for args on the standard output stream (System.out).
}
