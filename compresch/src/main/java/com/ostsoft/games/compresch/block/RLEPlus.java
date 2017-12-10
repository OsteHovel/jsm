package com.ostsoft.games.compresch.block;

import com.ostsoft.games.compresch.BlockList;

public class RLEPlus extends Block {

    public RLEPlus(int type) {
        super(type);
    }

    @Override
    public void build(BlockList blockList, byte[] src) {

        int srcLen = src.length;

        byte run;
        byte currun;

        int i = 0;
        int j;
        int len;

        while (i < srcLen) {
            run = src[i];
            currun = (byte) (run + 1);

            for (j = i + 1; j < srcLen; ++j) {
                if (src[j] != currun) {
                    break;
                }
                currun = (byte) (currun + 1);
            }

            len = j - i;

            if (len >= getMinLength()) {
                RLEPlus bk = new RLEPlus(type);
                bk.start = i;
                bk.stop = j;
                bk.len = len;
                bk.dat = run;

                blockList.insertBlock(bk);
            }

            i = j;
        }
    }

    @Override
    public int getBodySize() {
        return 1;
    }

    @Override
    public int getMinLength() {
        return getBodySize() + 2;
    }

    @Override
    public int decompress(int length, int[] src, int srcPos, int[] dst, int[] dstPosArray) {
        int dstPos = dstPosArray[0];

        int val = src[srcPos];
        for (int i = 0; i < length; i++, val++) {
            dst[dstPos + i] = val;
        }
        dstPos += length;

        dstPosArray[0] = dstPos;
        return 1;
    }

    @Override
    public boolean shrink(int newstart, int newstop) {
        dat = (byte) (dat + (newstart - start));
        start = newstart;
        stop = newstop;
        len = stop - start;

        return false;
    }

    @Override
    public Block dup() {
        RLEPlus bk = new RLEPlus(type);
        bk.start = start;
        bk.stop = stop;
        bk.len = len;
        bk.dat = dat;
        return bk;
    }

    @Override
    public int output(byte[] dst, int dstPos) {
        dst[dstPos] = dat;
        return getBodySize();
    }
}
