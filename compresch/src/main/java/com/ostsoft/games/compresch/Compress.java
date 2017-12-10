package com.ostsoft.games.compresch;

import com.ostsoft.games.compresch.block.Block;
import com.ostsoft.games.compresch.block.LZ2;
import com.ostsoft.games.compresch.block.RLE;
import com.ostsoft.games.compresch.block.RLEAlternative;
import com.ostsoft.games.compresch.block.RLEPlus;

import java.util.ArrayList;
import java.util.List;

public class Compress {
    private Compress() {
    }

    public static byte[] compress(byte[] src) {
        List<Block> blocksChipers = new ArrayList<>(7);
        blocksChipers.add(new RLE(1));
        blocksChipers.add(new RLEAlternative(2));
        blocksChipers.add(new RLEPlus(3));
        blocksChipers.add(new LZ2(4, LZ2.AddressType.LITTLE_ENDIAN, LZ2.Operation.NONE, LZ2.Offset.ABSOLUTE));
        blocksChipers.add(new LZ2(5, LZ2.AddressType.LITTLE_ENDIAN, LZ2.Operation.XOR, LZ2.Offset.ABSOLUTE));
        blocksChipers.add(new LZ2(6, LZ2.AddressType.ONE_BYTE, LZ2.Operation.NONE, LZ2.Offset.RELATIVE));
        blocksChipers.add(new LZ2(7, LZ2.AddressType.ONE_BYTE, LZ2.Operation.XOR, LZ2.Offset.RELATIVE));

        BlockList blocks = new BlockList();
        for (Block blocksChiper : blocksChipers) {
            blocksChiper.build(blocks, src);
        }

//        blocksChipers.get(5).build(blocks, src);

        System.out.println("Number of blocks: " + blocks.size());


        byte[] dst = new byte[0x10000];
        return StdBlock.CrunchAndOutput(blocks, src, dst);
    }

    public static byte[] decompress(byte[] src) {
        int[] ints = new int[src.length];
        for (int i = 0; i < src.length; i++) {
            ints[i] = Byte.toUnsignedInt(src[i]);
        }
        return decompress(ints);
    }


    public static byte[] decompress(int[] src) {

        List<Block> blocks = new ArrayList<>();
        blocks.add(new RLE(1));
        blocks.add(new RLEAlternative(2));
        blocks.add(new RLEPlus(3));
        blocks.add(new LZ2(4, LZ2.AddressType.LITTLE_ENDIAN, LZ2.Operation.NONE, LZ2.Offset.ABSOLUTE));
        blocks.add(new LZ2(5, LZ2.AddressType.LITTLE_ENDIAN, LZ2.Operation.XOR, LZ2.Offset.ABSOLUTE));
        blocks.add(new LZ2(6, LZ2.AddressType.ONE_BYTE, LZ2.Operation.NONE, LZ2.Offset.RELATIVE));
        blocks.add(new LZ2(7, LZ2.AddressType.ONE_BYTE, LZ2.Operation.XOR, LZ2.Offset.RELATIVE));

        int srcPos = 0;
        int len = 0;

        int dstPos = 0;
        int[] dst = new int[0x10000];

        while (srcPos < src.length) {
            int op = src[srcPos];
            srcPos++;

            if (op == 0xFF) {
                break;
            }

            if ((op & 0xE0) == 0xE0) {
                len = ((op & 3) << 8) | src[srcPos];
                op = (op >> 2) & 7;

                srcPos++;
            }
            else {
                len = (op & 0x1F);

                op >>= 5;
            }

            len++;

            if (op > 0) {
                if (blocks.size() > op - 1) {
                    int[] dstPosArray = new int[1];
                    dstPosArray[0] = dstPos;

                    srcPos += blocks.get(op - 1).decompress(len, src, srcPos, dst, dstPosArray);

                    dstPos = dstPosArray[0];
                }
                else {
                    System.out.println("Missing block with op: 0x" + Integer.toHexString(op - 1));
                }
            }
            else {
                for (int i = 0; i < len; i++) {
                    dst[dstPos] = src[srcPos];
                    dstPos++;
                    srcPos++;
                }
            }
        }

//        System.out.println("Source pos: " + srcPos);

        byte[] decompressedData = new byte[dstPos];
        for (int i = 0; i < dstPos; i++) {
            decompressedData[i] = (byte) dst[i];
        }
        return decompressedData;
    }

}
