package com.ostsoft.games.jsm.util;

public class ThreeByte {
    public final int b1; // Least significant
    public final int b2;
    public final int b3; // Most significant - Usually Bank

    public ThreeByte(int b1, int b2, int b3) {
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public ThreeByte(int value) {
        this.b1 = value & 0xFF;
        this.b2 = (value >> 8) & 0xFF;
        this.b3 = (value >> 16) & 0xFF;
    }

    public ThreeByte(int value, int b3) {
        this.b1 = value & 0xFF;
        this.b2 = (value >> 8) & 0xFF;
        this.b3 = b3;
    }

    public int getInt() {
        return ((b3 << 16) | b2 << 8) | b1;
    }

    public int getReversedInt() {
        return ((b1 << 16) | b2 << 8) | b3;
    }

    public int getShort() {
        return (b2 << 8) | b1;

    }
}
