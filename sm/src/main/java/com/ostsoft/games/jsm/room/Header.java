package com.ostsoft.games.jsm.room;

import com.ostsoft.games.jsm.util.ByteStream;

public class Header {
    public int roomIndex;
    public int region;
    public int x;
    public int y;
    public int width;
    public int height;
    public int upScroller;
    public int downScroller;
    public int specialGraphicsBitFlags;
    public int doorOut;

    public Header(ByteStream stream) {
        roomIndex = stream.readUnsignedByte();
        region = stream.readUnsignedByte();
        x = stream.readUnsignedByte();
        y = stream.readUnsignedByte();
        width = stream.readUnsignedByte();
        height = stream.readUnsignedByte();
        upScroller = stream.readUnsignedByte();
        downScroller = stream.readUnsignedByte();
        specialGraphicsBitFlags = stream.readUnsignedByte();
        doorOut = stream.readReversedUnsignedShort() | (0x8F << 16);
    }

    public void printDebug() {
        System.out.println("Room Index: " + Integer.toHexString(roomIndex));
        System.out.println("Region: " + Integer.toHexString(region));
        System.out.println("X: " + Integer.toHexString(x));
        System.out.println("Y: " + Integer.toHexString(y));
        System.out.println("Width: " + Integer.toHexString(width));
        System.out.println("Height: " + Integer.toHexString(height));
        System.out.println("Up Scroller: " + Integer.toHexString(upScroller));
        System.out.println("Down Scroller: " + Integer.toHexString(downScroller));
        System.out.println("SpecialGraphicsBitFlags: " + Integer.toHexString(specialGraphicsBitFlags));

    }
}
