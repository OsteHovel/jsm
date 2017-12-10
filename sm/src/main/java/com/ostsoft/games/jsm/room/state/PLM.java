package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;

public class PLM {

    public int command;
    public int x;
    public int y;
    public int arguments;

    public PLM(ByteStream stream) {
        command = stream.readReversedUnsignedShort();
        x = stream.readUnsignedByte();
        y = stream.readUnsignedByte();
        arguments = stream.readReversedUnsignedShort();
    }

    public void printDebug() {
        System.out.println("PLM Command: 0x" + Integer.toHexString(command).toUpperCase() + "  " + command);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("Arguments: " + arguments);
    }
}
