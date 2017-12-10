package com.ostsoft.games.compresch;

import com.ostsoft.games.compresch.block.Block;

public class StdBlock {

    public static byte[] CrunchAndOutput(BlockList blocks, byte[] src, byte[] dst) {
        int i = 0;
        int dstpos = 0;
        int srcpos = 0;

        int srclen = src.length;

        Block blk;

        // break the blocks down
        SizeLimits[] sizeLimits = new SizeLimits[8];

        // ---------------------------------------

        // output the blocks
        for (i = 0; i < 8; ++i) {
            sizeLimits[i] = new SizeLimits(1, 32, 1024);
//            sizeLimits[i].blocksize      = 1;    // 1 byte header
//            sizeLimits[i].extrabyteafter = 32;   // use a 2 byte header if the block length exceeds 32
//            sizeLimits[i].lenmax         = 1024; // max length of 1024 (even when using a 2 byte header)
        }


        blocks.crunchList(sizeLimits, sizeLimits, srclen); // 1 byte block header -- 2 bytes after len of 32 -- max len of 1024

        while (srcpos < srclen) {
            blk = blocks.getMLst().pollFirst();

            if (blk == null)                    // if there are no more blocks in the list
            {                            //  output the rest of the data as straight copy (not compressed; "raw")
                System.out.println("! RAW ! at " + srcpos + " of " + src.length + " dstPos: " + dstpos);
                dstpos += OutputRawBlock(src, srcpos, dst, dstpos, srclen - srcpos);
                srcpos = srclen;
            }
            else {
                // otherwise, see if the next block starts further ahead than we are currently at
                // if the block starts up a ways, we need to output raw data up until the start of the next block
                if (blk.start > srcpos) {
                    int len = blk.start - srcpos;
                    System.out.println("Block RAW srcPos: " + srcpos + " dstPos: " + dstpos + " RAW length: " + len);
                    dstpos += OutputRawBlock(src, srcpos, dst, dstpos, len);
                }
                else {
//                    System.out.println("Block: " + blk.type + " srcPos: " + srcpos + " dstPos: " + dstpos + " dstLen: ");
                }

                // then output the compressed block
                int blockLen = OutputBlock(blk, dst, dstpos);
                System.out.println("Block:  " + blk.type + " srcPos: " + srcpos + " dstPos: " + dstpos + " start: " + blk.start + " stop: " + blk.stop + " len: " + blk.len);
                dstpos += blockLen;

                // set source position to the end of the block
                srcpos = blk.stop;

                // and delete the block (must be done because BlockList no longer owns it after being popped)
                //delete block;
            }
        }
//
//        // this is just a termination byte used by the nintendo format
        dst[dstpos] = unsignedIntToSignedByte(0xFF);

//
//        return dstpos + 1;
//    }

        return dst;
    }

    private static int OutputRawBlock(byte[] src, int srcpos, byte[] dst, int dstpos, int len) {
        int ret = 0;

        // ------------------------------

        while (len > 1024) {
            dst[dstpos] = unsignedIntToSignedByte(0xE3);
            dst[dstpos + 1] = unsignedIntToSignedByte(0xFF);
            System.arraycopy(src, srcpos, dst, dstpos + 2, 1024);

            dstpos += 1024 + 2;
            srcpos += 1024;
            ret += 1024 + 2;
            len -= 1024;
        }

        if (len > 32) {
            dst[dstpos] = unsignedIntToSignedByte(0xE0 | (((len - 1) >> 8) & 3));
            dst[dstpos + 1] = unsignedIntToSignedByte((len - 1) & 0xFF);
            System.arraycopy(src, srcpos, dst, dstpos + 2, len);

            return ret + len + 2;
        }

        dst[dstpos] = unsignedIntToSignedByte((len - 1) & 0x1F);
        System.arraycopy(src, srcpos, dst, dstpos + 1, len);

        return ret + len + 1;
    }

    private static int OutputBlock(Block blk, byte[] dst, int dstPos) {
        int len = blk.len - 1;
        if (blk.len > 32) {
            dst[dstPos] = unsignedIntToSignedByte(0xE0 | (blk.type << 2) | ((len >> 8) & 3));
            dst[dstPos + 1] = unsignedIntToSignedByte(len & 0xFF);

            return 2 + blk.output(dst, dstPos + 2);
        }
        else if (blk.type == 7) {
            dst[dstPos] = unsignedIntToSignedByte(0xE0 | (blk.type << 2) | ((len >> 8) & 3));
            dst[dstPos + 1] = unsignedIntToSignedByte(len & 0xFF);

            return 2 + blk.output(dst, dstPos + 2);
        }

        dst[dstPos] = unsignedIntToSignedByte((blk.type << 5) | (len & 0x1F));

        return 1 + blk.output(dst, dstPos + 1);
    }

    public static byte unsignedIntToSignedByte(int unsignedInt) {
        return (byte) (unsignedInt & 0xFF);
    }
}
