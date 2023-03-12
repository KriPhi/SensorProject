package com.vu.lorasensorapp;

public class XXTEAdecrypt {
    private static final int DELTA = 0x9e3779b9;

    private static int mx(int sum, int y, int z, int p, int e, int[] v, int[] k) {
        return ((z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4)) ^ ((sum ^ y) + (k[p & 3 ^ e] ^ z));
    }

    public static void decrypt(int[] v, int n, int[] k) {
        int rounds, sum, e, p, y, z;
        if (n > 1) {          /* Coding Part */
            rounds = 6 + 52 / n;
            sum = rounds * DELTA;
            y = v[0];
            do {
                e = (sum >>> 2) & 3;
                for (p = n - 1; p > 0; p--) {
                    z = v[p - 1];
                    y = v[p] -= mx(sum, y, z, p, e, v, k);
                }
                z = v[n - 1];
                y = v[0] -= mx(sum, y, z, p, e, v, k);
                sum -= DELTA;
            } while (sum != 0);
        }
    }
}
