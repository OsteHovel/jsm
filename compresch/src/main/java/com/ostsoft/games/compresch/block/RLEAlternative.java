package com.ostsoft.games.compresch.block;

import com.ostsoft.games.compresch.BlockList;

public class RLEAlternative extends Block {
    private byte[] dat = new byte[2];

    public RLEAlternative(int type) {
        super(type);
    }

    @Override
    public void build(BlockList blockList, byte[] src) {
        int srcLen = src.length;
        byte[] run = new byte[2];

        int i = 0;
        int j;
        int len;

        while (i < srcLen) {
            if (i + 1 >= srcLen) {
                System.out.println("Too long");
                break;
            }

            run[0] = src[i];
            run[1] = src[i + 1];

            len = 0;

            for (j = i + 2; j < srcLen; ++j, len ^= 1) {
                if (src[j] != run[len]) {
                    break;
                }
            }

            len = j - i;

            if (len >= getMinLength()) {
                RLEAlternative bk = new RLEAlternative(type);
                bk.start = i;
                bk.stop = j;
                bk.len = len;
                bk.dat[0] = run[0];
                bk.dat[1] = run[1];

                blockList.insertBlock(bk);
            }

            i = j - 1;
        }
    }

    @Override
    public int getBodySize() {
        return 2;
    }

    @Override
    public int getMinLength() {
        return getBodySize() + 2;
    }

    @Override
    public int decompress(int length, int[] src, int srcPos, int[] dst, int[] dstPosArray) {
        int dstPos = dstPosArray[0];

        int i;
        for (i = 0; i < length; ++i) {
            dst[dstPos + i] = src[srcPos + (i & 1)];
        }
        dstPos += length;

        dstPosArray[0] = dstPos;
        return 2;
    }

    @Override
    public boolean shrink(int newstart, int newstop) {
        if (((newstart ^ start) & 1) != 0) {
            byte tmp = dat[0];
            dat[0] = dat[1];
            dat[1] = tmp;
        }

        start = newstart;
        stop = newstop;
        len = stop - start;

        return false;
    }

    @Override
    public Block dup() {
        RLEAlternative bk = new RLEAlternative(type);
        bk.start = start;
        bk.stop = stop;
        bk.len = len;
        bk.dat = dat;

        return bk;
    }

    @Override
    public int output(byte[] dst, int dstPos) {
        dst[dstPos] = dat[0];
        dst[dstPos + 1] = dat[1];
//        System.arraycopy(dat, 0, dst, dstPos, dat.length);
        return getBodySize();
    }


}

