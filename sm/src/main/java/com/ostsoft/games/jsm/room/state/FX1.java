package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;

public class FX1 {
    public int select; // 0000 or doorID, use this entry... FFFF none... anything else, add 10h to FX1 pointer, and loop back to find next entry
    public int surfaceStart; // starting point of liquid's surface
    public int surfaceNew; // new surface of liquid
    public int surfaceSpeed; // speed of surface of liquid (lower is faster, bit 15 selects direction(0=flow down))
    public int surfaceDelay; // lower is faster (0 = LONG time)
    public int layer3Type;
    public int a;
    public int b;
    public int c;
    public int paletteFX;
    public int animateTile;
    public int paletteBlend;

    public FX1(ByteStream stream) {
        select = stream.readReversedUnsignedShort();
        surfaceStart = stream.readReversedUnsignedShort();
        surfaceNew = stream.readReversedUnsignedShort();
        surfaceSpeed = stream.readReversedUnsignedShort();
        surfaceDelay = stream.readUnsignedByte();
        layer3Type = stream.readUnsignedByte();
        a = stream.readUnsignedByte();
        b = stream.readUnsignedByte();
        c = stream.readUnsignedByte();
        paletteFX = stream.readUnsignedByte();
        animateTile = stream.readUnsignedByte();
        paletteBlend = stream.readUnsignedByte();
    }

    public void printDebug() {
        System.out.println("Select: 0x" + Integer.toHexString(select).toUpperCase() + " - " + select);
        System.out.println("surfaceStart: 0x" + Integer.toHexString(surfaceStart).toUpperCase() + " - " + surfaceStart);
        System.out.println("surfaceNew: 0x" + Integer.toHexString(surfaceNew).toUpperCase() + " - " + surfaceNew);
        System.out.println("surfaceSpeed: 0x" + Integer.toHexString(surfaceSpeed).toUpperCase() + " - " + surfaceSpeed);
        System.out.println("surfaceDelay: 0x" + Integer.toHexString(surfaceDelay).toUpperCase() + " - " + surfaceDelay);
        System.out.println("layer3Type: 0x" + Integer.toHexString(layer3Type).toUpperCase() + " - " + layer3Type);
        System.out.println("a: 0x" + Integer.toHexString(a).toUpperCase() + " - " + a);
        System.out.println("b: 0x" + Integer.toHexString(b).toUpperCase() + " - " + b);
        System.out.println("c: 0x" + Integer.toHexString(c).toUpperCase() + " - " + c);
        System.out.println("paletteFX: 0x" + Integer.toHexString(paletteFX).toUpperCase() + " - " + paletteFX);
        System.out.println("animateTile: 0x" + Integer.toHexString(animateTile).toUpperCase() + " - " + animateTile);
        System.out.println("paletteBlend: 0x" + Integer.toHexString(paletteBlend).toUpperCase() + " - " + paletteBlend);

    }
}
