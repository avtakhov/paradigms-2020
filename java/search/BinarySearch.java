package search;

public class BinarySearch {

    // PRE: a != null, n = a.length, (a[0] >= a[1] >= ... >= a[n - 1] || n == 1), x >= a[n - 1]
    // POST: T = {index i -> a[i] <= x}, search(x, a) is minimum of T
    private static int search(int x, int[] a) {
        // INV: (l == -1 || a[l] > x) && l < n && l >= -1
        int l = -1;
        // INV: (r == n || a[r] <= x) && r <= n && r >= 0
        int r = a.length;
        // n >= 1 => n - (-1) > 1 => r - l > 1
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
        // r != n because x >= a[n - 1] => a[n - 1] is a member of T
        return r;
    }

    // PRE: args != null && args.length >= 2 && forAll i in (0..args.length - 1) args[i] correct integer in string format
    // and pre of search() too
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.println(search(x, a));
    }
}
