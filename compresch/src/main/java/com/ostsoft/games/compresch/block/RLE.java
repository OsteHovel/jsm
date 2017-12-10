package com.ostsoft.games.compresch.block;

import com.ostsoft.games.compresch.BlockList;

public class RLE extends Block {

    public RLE(int type) {
        super(type);
    }

    @Override
    public void build(BlockList blockList, byte[] src) {
        byte run;

        int i, j;
        int len;


        // ---------------------
        i = 0;
        while (i < src.length) {
            run = src[i];

            for (j = i + 1; j < src.length; ++j) {
                if (src[j] != run) {
                    break;
                }
            }

            len = j - i;

            // RLE isn't worth it unless the run length is 2 or more
            if (len >= getMinLength()) {
                RLE bk = new RLE(type);
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

        while (length > 0) {
            dst[dstPos++] = src[srcPos];
            length--;
        }

        dstPosArray[0] = dstPos;
        return 1;
    }

    @Override
    public boolean shrink(int newstart, int newstop) {
        start = newstart;
        stop = newstop;
        len = stop - start;
        return false;
    }

    @Override
    public Block dup() {
        RLE bk = new RLE(type);
        bk.start = start;
        bk.stop = stop;
        bk.len = len;
        bk.dat = dat;
        return bk;
    }

    @Override
    public int output(byte[] dst, int dstPos) {
        dst[dstPos] = dat;
        return 1;
    }
}