package com.ostsoft.games.jsm.editor;

import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Compare {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        File file = new File("sm.smc");
        File fileorig = new File("smorig.smc");

        ByteStream stream = getByteStreamFromFile(file);
        ByteStream stream2 = getByteStreamFromFile(fileorig);

        for (int i = 0; i < stream.size(); i++) {
            int aByte = stream.readUnsignedByte();
            int aByte2 = stream2.readUnsignedByte();
            if (aByte != aByte2) {
                System.out.println("Differs at PC: " + Integer.toHexString(i).toUpperCase() + " SNES: " + Integer.toHexString(BitHelper.pcToSnes(i)).toUpperCase() + " with 0x" + Integer.toHexString(aByte).toUpperCase() + " != " + Integer.toHexString(aByte2).toUpperCase());
            }
        }

    }

    private static ByteStream getByteStreamFromFile(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            int len = stream.available();
            byte[] bytes = new byte[len];
            stream.read(bytes, 0, len);
            return new ByteStream(bytes);
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }
        return null;
    }

}
