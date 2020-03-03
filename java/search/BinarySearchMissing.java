package search;

public class BinarySearchMissing {

    // PRE: a != null, n = a.length, (a[0] >= a[1] >= ... >= a[n - 1] || n == 1 || n == 0)
    // POST: T = {index i -> a[i] <= x}, searchMissing(x, a) is max of T if (x in a) else
    // searchMissing(x, a) is (- 1 - insertionPos). insertionPos - max of T
    private static int searchMissing(int x, int[] a) {
        // INV: (l == -1 || a[l] > x) && l < n && l >= -1
        int l = -1;
        // INV: (r == n || a[r] <= x) && r <= n && r >= 0
        int r = a.length;

        // INV: l == -1 || r == n || a[l] < a[r]
        while (r - l > 1) {
            // INV: m > -1 && m < n : if (m <= -1) => (l + r) <= -2, but -1 <= (l + r) <= n
            //                      : if (m >= n) => (l + r) >= n + 1, but -1 <= (l + r) <= n from inv l and inv r
            int m = (l + r) / 2;
            if (a[m] > x) {
                // a[l] > x, l > -1 and l < n, inv keeps
                l = m;
            } else {
                // a[r] <= x, r > -1 and r <= n, inv keeps
                r = m;
            }
        }
        // r == l + 1
        // r is minimum of T. if there is r0 minimum of T, then l < r0 <= r1, but l + 1 is r => r0 equals r

        if (r != a.length && a[r] == x) {
            return r;
        } else {
            return -1 - r;
        }
    }


    // PRE: a != null, n = a.length, (a[0] >= a[1] >= ... >= a[n - 1] || n == 1 || n == 0)
    // PRE + INV: (l == -1 || a[l] > x) && l < n && l >= -1
    // PRE + INV: (r == n || a[r] <= x) && r <= n && r >= 0
    // PRE + INV: l == -1 || r == n || a[l] < a[r]
    // PRE: -1 <= l < n &&  0 <= r <= n
    // l + 1 <= r
    // POST: T = {index i -> a[i] <= x}, searchMissing(x, a) is minimum of T if (x in a) else

    // searchMissing(x, a) is (- 1 - insertionPos). insertionPos - minimum of T
    private static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        if (r - l <= 1) {
            // l + 1 == r
            if (r != a.length && a[r] == x) {
                return r;
            } else {
                // r == a.length => x not in r
                // a[r] != x => x not in r
                // because a[l] > x
                return -1 - r;
            }
        }
        int m = (l + r) / 2;
        // INV: m > -1 && m < n : if (m <= -1) => (l + r) <= -2, but -1 <= (l + r) <= n
        //                      : if (m >= n) => (l + r) >= n + 1, but -1 <= (l + r) <= n from inv l and inv r

        if (a[m] > x) {
            // a[l] > x, l > -1 and l < n, inv keeps
            return recursiveBinarySearch(x, a, m, r);
        } else {
            // a[r] <= x, r > -1 and r <= n, inv keeps
            return recursiveBinarySearch(x, a, l, m);
        }
    }

    private static int recursiveSearchMissing(int x, int[] a) {
        return recursiveBinarySearch(x, a, -1, a.length);
    }

    // PRE: args != null && args.length >= 1 && forAll i in (0..args.length - 1) args[i] correct integer in string format
    // and pre of search() too
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        assert searchMissing(x, a) == recursiveSearchMissing(x, a);
        System.out.println(searchMissing(x, a));
    }
}
