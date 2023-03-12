package com.vu.lorasensorapp;

public class TEAdecrypt {
    private static final int[] DELTA = {0x9E3779B9, 0x9E3779B9};
    private static int[] key = {0x23, 0x89, 0xAB, 0xEF};

    public static int decrypt(int v) {
        int y = v >>> 16, z = v & 0xFFFF;
        int sum = 0xC6EF3720;
        int n = 32;

        while (n-- > 0) {
            z -= (y << 4 ^ y >>> 5) + y ^ sum + key[sum >>> 11 & 3];
            sum -= DELTA[1];
            y -= (z << 4 ^ z >>> 5) + z ^ sum + key[sum & 3];
        }

        return (y << 16) | (z & 0xFFFF);
    }

    public int decrypt(int v, int[] k) {
        int v0 = v >>> 16, v1 = v & 0xFFFF, sum = 0xC6EF3720, i;
        int delta = 0x9E3779B9;
        int k0 = k[0], k1 = k[1], k2 = k[2], k3 = k[3];
        for (i = 0; i < 32; i++) {
            v1 -= ((v0 << 4) + k2) ^ (v0 + sum) ^ ((v0 >>> 5) + k3);
            v0 -= ((v1 << 4) + k0) ^ (v1 + sum) ^ ((v1 >>> 5) + k1);
            sum -= delta;
        }
        return (v0 << 16) | (v1 & 0xFFFF);
    }
}