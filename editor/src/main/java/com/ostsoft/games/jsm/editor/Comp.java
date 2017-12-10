package com.ostsoft.games.jsm.editor;

import com.ostsoft.games.compresch.Compress;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Comp {

    public static void main(String[] args) {
        try {
            File file = new File("c.bin");
            FileInputStream fileInputStream = new FileInputStream(file);
            int len = fileInputStream.available();
            byte[] bytes = new byte[len];
            fileInputStream.read(bytes, 0, len);
            ByteStream stream = new ByteStream(bytes);
            int offset = 0;
//            int offset = BitHelper.snesToPc(Pointer.CreditsTiles.pointer);
//            byte[] tiles = Compress.decompress(stream, offset);
//            byte[] tiles = Lunar.decompress("sm.smc", offset);
            byte[] tiles = stream.getBuffer();

            System.out.println("Length of compressed data in file: " + (stream.getPosition() - offset));
            System.out.println("Uncompressed length: " + tiles.length);

            FileOutputStream fileOutputStream = new FileOutputStream("uc.bin");
            fileOutputStream.write(tiles);


            byte[] compress = Compress.compress(tiles);
            int compLen = 0;
            for (int i = compress.length - 1; i >= 0; i--) {
                if (Byte.toUnsignedInt(compress[i]) == 0xFF) {
                    compLen = i;
                    break;
                }
            }
            System.out.println("Length of newly compressed data: " + compLen);

            FileOutputStream fileOutputStream2 = new FileOutputStream("ccc.bin");
            fileOutputStream2.write(compress);

            ByteStream stream1 = new ByteStream(compress);
            byte[] tiles2 = CompressUtil.decompress(stream1, 0);
            System.out.println("Uncompressed of compressed length: " + tiles2.length);

            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i] != tiles2[i]) {
                    System.out.println("Not the same at " + i);
                    System.out.println("Ending compare");
                    return;
                }
            }

            System.out.println("The uncompress, compress, recompress are the same!");

//            System.out.println("LUNAR\n-------------------------------------------------");
//            byte[] lcCompress = Lunar.compress(tiles2);
//            byte[] decompLC = Compress.decompress(lcCompress);
//            for (int i = 0; i < decompLC.length; i++) {
//                if (decompLC[i] != tiles[i]) {
//                    System.out.println("Not the same at " + i);
//                    System.out.println("Ending compare");
//                    return;
//                }
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String hex(int i2) {
        return Integer.toHexString(i2).toUpperCase();
    }

    private static String hex(byte b) {
        return Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase();
    }

}

