package com.ostsoft.games.jsm.util;

import com.ostsoft.games.compresch.Compress;

public class CompressUtil {
    public static byte[] decompress(ByteStream stream, int offset) {
        stream.setPosition(offset);
        int totalLength = 0x10000;
        if (totalLength > stream.size()) {
            totalLength = stream.size();
        }

        int[] src = new int[totalLength];
        for (int i = 0; i < totalLength; i++) {
            src[i] = stream.readUnsignedByte();
        }

        return Compress.decompress(src);
    }
}