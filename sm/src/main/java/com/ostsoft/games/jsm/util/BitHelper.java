package com.ostsoft.games.jsm.util;

import java.io.DataInputStream;
import java.io.IOException;

public class BitHelper {
    private BitHelper() {

    }

    public static byte unsignedIntToSignedByte(int signedInt) {
        return (byte) (signedInt & 0xFF);
    }

    public static int readUnsignedShortReversed(DataInputStream stream) throws IOException {
        return Short.toUnsignedInt(Short.reverseBytes(stream.readShort()));
    }

    public static ThreeByte readThreeBytes(DataInputStream stream) throws IOException {
        return new ThreeByte(stream.readUnsignedByte(), stream.readUnsignedByte(), stream.readUnsignedByte());
    }

    public static ThreeByte readTwoBytesReturnThree(DataInputStream stream, int b3) throws IOException {
        return new ThreeByte(stream.readUnsignedByte(), stream.readUnsignedByte(), b3);
    }

    public static int threeByteToOffset(ThreeByte threeByte) {
        int b3 = threeByte.b3;

        if (b3 >= 0x80) {
            b3 = (b3 - 0x80);
        }
        int tmp = threeByte.b2 << 8 | threeByte.b1;
        if (tmp < 0x8000) {
            tmp = tmp + 0x8000;
        }
        tmp = tmp + (b3 * 0x8000) - 0x8000;

        return tmp;
    }

    public static int snesToPc(int snesAddress) {
        int b3 = snesAddress >> 16;
        int b2 = (snesAddress >> 8) & 0xFF;
        int b1 = snesAddress & 0xFF;

        if (b3 >= 0x80) {
            b3 = (b3 - 0x80);
        }
        int tmp = b2 << 8 | b1;
        if (tmp < 0x8000) {
            tmp = tmp + 0x8000;
        }
        tmp = tmp + (b3 * 0x8000) - 0x8000;

        return tmp;
    }

    public static int snesToPc2(int snesAddress) {
        int addrlo = (snesAddress) & 0xFF;
        int addrhi = (snesAddress >> 8) & 0xFF;
        int bank = (snesAddress >> 16) & 0xFF;

        return (addrlo & 255) + (256 * (addrhi & 255)) + (32768 * (bank & 127)) - 512 - 32256;
    }


    public static int pcToSnes(int position) {
        int romPointer = position & 0x7FFFFF;
        romPointer = ((romPointer & 0x3F8000) << 1) + (romPointer & 0x7FFF) | 0x8000;
        return romPointer + 0x800000;
    }

    public static String readString(DataInputStream stream, int numberOfChar) throws IOException {
        char[] chars = new char[numberOfChar];
        for (int i = 0; i < numberOfChar; i++) {
            chars[i] = (char) stream.readUnsignedByte();
        }
        return new String(chars).trim();
    }
}
