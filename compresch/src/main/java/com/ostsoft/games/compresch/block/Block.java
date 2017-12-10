package com.ostsoft.games.compresch.block;


import com.ostsoft.games.compresch.BlockList;

public abstract class Block {
    public final int type;
    public int start;
    public int stop;
    public int len;
    protected byte dat;

    protected Block(int type) {
        this.type = type;
    }

    public abstract void build(BlockList blockList, byte[] src);

    public abstract int getBodySize();

    public abstract int getMinLength();

    public abstract int decompress(int length, int[] src, int srcPos, int[] dst, int[] dstPos);

    public int hasExtraByte() {
        return (type == 7) ? 1 : 0;
    }

    public boolean canShrink(int newstart, int newstop) {
        Block blk = this.dup();
        return !blk.shrink(newstart, newstop);

    }

    public abstract boolean shrink(int newstart, int newstop);

    public abstract Block dup();

    public void dropLen(int newLen) {
        shrink(start, start + newLen);
    }

    public abstract int output(byte[] dst, int dstPos);
}
