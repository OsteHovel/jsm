package com.ostsoft.games.compresch;

public class SizeLimits {
    public final int blockSize;
    public final int extraByteAfter;
    public final int lenMax;

    public SizeLimits(int blockSize, int extraByteAfter, int lenMax) {
        this.blockSize = blockSize;
        this.extraByteAfter = extraByteAfter;
        this.lenMax = lenMax;
    }
}
