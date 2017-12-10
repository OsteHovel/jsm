package com.ostsoft.games.jsm.animation.tile;

import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class TileEntry {
    private final int index;

    private int tableStart;
    private int bytesPerFrame;
    private int vramPlacement;
    private TileAnimation tileAnimation;

    public TileEntry(int index) {
        this.index = index;
    }

    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(0x87824B));
        stream.seek(index * 6);

        tableStart = stream.readReversedUnsignedShort() | (0x87 << 16);
        bytesPerFrame = stream.readReversedUnsignedShort();
        vramPlacement = stream.readReversedUnsignedShort();


        tileAnimation = new TileAnimation(tableStart, index);
        tileAnimation.load(stream);
    }

    public void save(ByteStream stream) {
        stream.writeUnsignedReversedShort(tableStart);
        stream.writeUnsignedReversedShort(bytesPerFrame);
        stream.writeUnsignedReversedShort(vramPlacement);
    }

    public int getTableStart() {
        return tableStart;
    }

    public void setTableStart(int tableStart) {
        this.tableStart = tableStart;
    }

    public int getBytesPerFrame() {
        return bytesPerFrame;
    }

    public void setBytesPerFrame(int bytesPerFrame) {
        this.bytesPerFrame = bytesPerFrame;
    }

    public int getVramPlacement() {
        return vramPlacement;
    }

    public void setVramPlacement(int vramPlacement) {
        this.vramPlacement = vramPlacement;
    }

    public void printDebug() {
        System.out.println("tableStart: " + tableStart);
        System.out.println("bytesPerFrame: " + bytesPerFrame);
        System.out.println("vramPlacement: " + vramPlacement);
    }

    public TileAnimation getTileAnimation() {
        return tileAnimation;
    }
}
