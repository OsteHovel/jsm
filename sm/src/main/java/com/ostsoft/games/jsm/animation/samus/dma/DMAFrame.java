package com.ostsoft.games.jsm.animation.samus.dma;

import com.ostsoft.games.jsm.util.ByteStream;

public class DMAFrame {

    public int indexTopHalfTable;
    public int indexTopHalfEntry;
    public int indexBottomHalfTable;
    public int indexBottomHalfEntry;

    public DMAFrame(ByteStream stream) {
        indexTopHalfTable = stream.readUnsignedByte();
        indexTopHalfEntry = stream.readUnsignedByte();
        indexBottomHalfTable = stream.readUnsignedByte();
        indexBottomHalfEntry = stream.readUnsignedByte();
    }

    public void save(ByteStream stream) {
        indexTopHalfTable = Math.max(Math.min(indexTopHalfTable, 0x0C), 0);
        indexTopHalfEntry = Math.max(Math.min(indexTopHalfEntry, 0x1F), 0);
        indexBottomHalfTable = Math.max(Math.min(indexBottomHalfTable, 0x0A), 0);
        indexBottomHalfEntry = Math.max(Math.min(indexBottomHalfEntry, 0x1F), 0);


        stream.writeUnsignedByte(indexTopHalfTable);
        stream.writeUnsignedByte(indexTopHalfEntry);
        stream.writeUnsignedByte(indexBottomHalfTable);
        stream.writeUnsignedByte(indexBottomHalfEntry);
    }

    public void printDebug() {
        System.out.println("IndexTopHalfTable: " + Integer.toHexString(indexTopHalfTable).toUpperCase());
        System.out.println("IndexTopHalfEntry: " + Integer.toHexString(indexTopHalfEntry).toUpperCase());
        System.out.println("IndexBottomHalfTable: " + Integer.toHexString(indexBottomHalfTable).toUpperCase());
        System.out.println("IndexBottomHalfEntry: " + Integer.toHexString(indexBottomHalfEntry).toUpperCase());
    }
}
