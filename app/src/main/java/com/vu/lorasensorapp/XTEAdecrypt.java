package com.vu.lorasensorapp;

public class XTEAdecrypt {

    private static final int[] k = {0x23, 0x89, 0xAB, 0xEF};

    public static int decrypt(int v) {
        int num_rounds = 32; // number of rounds for XTEA algorithm
        int[] v_arr = new int[2];
        v_arr[0] = v >>> 16;
        v_arr[1] = v & 0xFFFF;
        int i;
        int v0 = v_arr[0], v1 = v_arr[1], delta = 0x9E3779B9, sum = delta * num_rounds;
        for (i = 0; i < num_rounds; i++) {
            v1 -= (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + k[(sum >>> 11) & 3]);
            sum -= delta;
            v0 -= (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + k[sum & 3]);
        }
        v_arr[0] = v0;
        v_arr[1] = v1;
        return (v_arr[0] << 16) | (v_arr[1] & 0xFFFF);
    }
}