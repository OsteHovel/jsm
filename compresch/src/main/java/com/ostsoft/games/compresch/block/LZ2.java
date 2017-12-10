package com.ostsoft.games.compresch.block;

import com.ostsoft.games.compresch.BlockList;
import com.ostsoft.games.compresch.StdBlock;

public class LZ2 extends Block {
    private final AddressType addressType;
    private final Operation operation;
    private final Offset offset;
    private int lz2dat;

    public LZ2(int type, AddressType addressType, Operation operation, Offset offset) {
        super(type);
        this.addressType = addressType;
        this.operation = operation;
        this.offset = offset;
    }

    @Override
    public void build(BlockList blockList, byte[] src) {
        int srcLen = src.length;

        int i = 1;
        int j = 0;
        int len = 0;

        int bestLen = 0;
        int bestStart = 0;
        int maxStart = getMaxStart(srcLen);

        boolean justBlocked = false;
        int adj = 0;
        int bestAdj = 0;

        // --------------------------------


        while (i < srcLen) {
            bestAdj = 0;
            bestLen = getMinLength() - 1;

            for (j = getMinStart(i); j < i; j++) {
                if (j > maxStart) {
                    break;
                }

                for (len = 0; ; len++) {
                    int mainPos = (i + len);
                    int auxPos = j + len;

                    // -------------------------

                    if ((mainPos < 0) || (mainPos >= srcLen)) {
                        break;
                    }

                    if ((auxPos < 0) || (auxPos >= srcLen)) {
                        break;
                    }

                    if (Byte.toUnsignedInt(src[mainPos]) != Operation(Byte.toUnsignedInt(src[auxPos]))) {
                        break;
                    }
                }

                adj = 0;

                if (justBlocked && len != 0) {
                    for (; ; adj++) {
                        int mainPos = (i - adj);
                        int auxPos = invertedAuxPos(j, adj);

                        // ---------------------------------------

                        if ((mainPos < 0) || (mainPos >= srcLen)) {
                            break;
                        }

                        if ((auxPos < 0) || (auxPos >= srcLen)) {
                            break;
                        }

                        // This is primarily here for the Backward directed
                        // LZ blocks, but this should never happen for Forward
                        // directed blocks anyways.
                        if (auxPos >= mainPos) {
                            System.out.println("?");
                            break;
                        }

                        if (Byte.toUnsignedInt(src[mainPos]) != Operation(Byte.toUnsignedInt(src[auxPos]))) {
                            break;
                        }
                    }

                    adj--;

                    len += adj;

                    if (len < getMinLength()) {
                        continue;
                    }
                }

                if (((adj == bestAdj) && (len > bestLen)) || (adj > bestAdj)) {
                    bestLen = len;
                    bestStart = invertedAuxPos(j, adj);
                    bestAdj = adj;
                }
            }

            if (bestLen >= getMinLength()) {
                LZ2 bk = new LZ2(this.type, this.addressType, this.operation, this.offset);
                bk.start = i - bestAdj;
                bk.stop = i - bestAdj + bestLen;
                bk.len = bestLen;
                bk.lz2dat = bestStart;

                if (bk.shrink(bk.start, bk.stop)) {
                    i++;
                    justBlocked = false;
                }
                else {
                    blockList.insertBlock(bk);
                    i = bk.stop;
                    justBlocked = true;
                }
            }
            else {
                i++;
                justBlocked = false;
            }
        }
    }

    private int invertedAuxPos(int aux, int len) {
        return aux - len;
    }

    private int getMinStart(int pos) {
        if (offset == Offset.RELATIVE) {
            int minStart = pos - getMaxDataSize();
            return (minStart < 0) ? 0 : minStart;
        }
        else if (offset == Offset.ABSOLUTE) {
            return 0;
        }
        else {
            System.out.println("[LZ2] getMinStart(): Invalid offset");
            return 0;
        }
    }

    private int getMaxStart(int srcLen) {
        if (offset == Offset.ABSOLUTE) {
            return getMaxDataSize();

        }
        else if (offset == Offset.RELATIVE) {
            return srcLen;
        }
        System.out.println("[LZ2] Invalid offset type");
        return 0;
    }

    private int getMaxDataSize() {
        // ----------------------------------
        // The range of our data is allowed to be
        // 0x00 to 0xFF, or 0x0000 to 0xFFFF
        int ret = (1 << (8 * getBodySize()));
        ret -= 1;
        return ret;
    }

    @Override
    public int getBodySize() {
        switch (addressType) {
            case LITTLE_ENDIAN:
                return 2;

            case ONE_BYTE:
                return 1;
        }
        System.out.println("[LZ2] Unsupported address type");
        return 0;
    }

    @Override
    public int getMinLength() {
        return getBodySize() + 2;
    }

    @Override
    public int decompress(int length, int[] src, int srcPos, int[] dst, int[] dstPosArray) {
        int dstPos = dstPosArray[0];
        int from = getOffset(src, srcPos, dstPos);
        if (from < 0) {
            System.out.println("[LZ2] Decompress() from is less than 0");
        }
        if (from >= dstPos) {
            System.out.println("[LZ2] Decompress() trying to read data that's after where i am, srcPos: " + srcPos + " dstPos: " + dstPos + " from: " + from);
            System.out.println("[LZ2] " + addressType.name() + " - " + offset.name() + " - " + operation.name());
        }
        else {
            for (int i = 0; i < length; ++i) {
                dst[dstPos + i] = Operation(dst[from + i]);
            }
        }
        dstPos += length;


        dstPosArray[0] = dstPos;
        if (addressType == AddressType.LITTLE_ENDIAN) {
            return 2;
        }
        else if (addressType == AddressType.ONE_BYTE) {
            return 1;
        }
        else {
            System.out.println("[LZ2] Decompress() No addressType");
            return 0;
        }
    }

    @Override
    public boolean shrink(int newstart, int newstop) {
        int newdat = (lz2dat + newstart - start);

        if (newdat > getMaxDataSize()) {
            return true;
        }

        lz2dat = newdat;
        start = newstart;
        stop = newstop;
        len = stop - start;

        return false;
    }

    @Override
    public Block dup() {
        LZ2 block = new LZ2(type, addressType, operation, offset);
        block.start = start;
        block.stop = stop;
        block.len = len;
        block.lz2dat = lz2dat;
        return block;
    }


    private int Operation(int input) {
        if (operation == Operation.NONE) {
            return input;
        }
        else if (operation == Operation.XOR) {
            return (input ^ 0xFF);
        }
        else {
            System.out.println("[LZ2] Operation() No Operation");
            return 0;
        }
    }

    private int getOffset(int[] src, int srcPos, int dstPos) {
        if (offset == Offset.RELATIVE) {
            return dstPos - getAddress(src, srcPos, dstPos);
        }
        else if (offset == Offset.ABSOLUTE) {
            return getAddress(src, srcPos, dstPos);
        }
        System.out.println("[LZ2] getOffset() No offset type");
        return 0;
    }

    private int getAddress(int[] src, int srcPos, int dstPos) {
        if (addressType == AddressType.LITTLE_ENDIAN) {
            return ((src[srcPos]) | (src[srcPos + 1] << 8));
        }
        else if (addressType == AddressType.ONE_BYTE) {
            return src[srcPos];
        }
        System.out.println("[LZ2] getAddress() No address type");
        return 0x0;
    }

    @Override
    public int output(byte[] dst, int dstPos) {
        setOffset(dst, dstPos);
        return getBodySize();
    }

    private void setOffset(byte[] buf, int dstPos) {
        setAddr(buf, lz2dat, dstPos);
    }

    private void setAddr(byte[] buf, int arg, int dstPos) {
        if (addressType == AddressType.LITTLE_ENDIAN) {
            buf[dstPos] = StdBlock.unsignedIntToSignedByte(arg & 0xFF);
            buf[dstPos + 1] = StdBlock.unsignedIntToSignedByte((arg >> 8) & 0xFF);
        }
        else if (addressType == AddressType.ONE_BYTE) {
            buf[dstPos] = StdBlock.unsignedIntToSignedByte(arg);
        }
        else {
            System.out.println("[LZ2] Unknown address type");
        }
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public Operation getOperation() {
        return operation;
    }

    public Offset getOffset() {
        return offset;
    }

    public int getLz2dat() {
        return lz2dat;
    }

    public enum AddressType {
        LITTLE_ENDIAN,
        ONE_BYTE
    }

    public enum Operation {
        NONE,
        XOR
    }

    public enum Offset {
        ABSOLUTE,
        RELATIVE
    }
}
