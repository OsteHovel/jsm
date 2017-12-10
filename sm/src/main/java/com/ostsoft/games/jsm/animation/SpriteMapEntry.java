package com.ostsoft.games.jsm.animation;

import com.ostsoft.games.jsm.util.ByteStream;

public class SpriteMapEntry {
    private int x;
    private int priorityA;
    private int y;
    private int tileIndex;
    private int priorityB;

    public void load(ByteStream stream) {
        x = stream.readUnsignedByte();
        priorityA = stream.readUnsignedByte();
        y = stream.readByte();
        tileIndex = stream.readUnsignedByte();
        priorityB = stream.readUnsignedByte();

        if (isXOffsetHigh()) {
            x -= 0x100;
        }
        if (isLoadNextGraphicsPage()) {
            tileIndex += 0x100;
        }
    }

    public void save(ByteStream stream) {
        if (x >= 0) {
            setXOffsetHigh(false);
        }
        else {
            setXOffsetHigh(true);
        }

        int tmpTileIndex = this.tileIndex;
        if (tmpTileIndex >= 0x100) {
            tmpTileIndex -= 0x100;
            setLoadNextGraphicsPage(true);
        }
        else {
            setLoadNextGraphicsPage(false);
        }


        int tmpX = x;
        if (isXOffsetHigh()) {
            tmpX += 0x100;
        }

        stream.writeUnsignedByte(tmpX);
        stream.writeUnsignedByte(priorityA);
        stream.writeUnsignedByte(y);
        stream.writeUnsignedByte(tmpTileIndex);
        stream.writeUnsignedByte(priorityB);
    }


    public void printDebug() {
        System.out.println("X: 0x" + Integer.toHexString(x).toUpperCase() + " - " + x);
        System.out.println("Priority A: 0b" + Integer.toBinaryString(priorityA) + " - 0x" + Integer.toHexString(priorityA).toUpperCase() + " - " + priorityA);
        System.out.println("Y: 0x" + Integer.toHexString(y).toUpperCase() + " - " + y);
        System.out.println("TileIndex: " + tileIndex);
        System.out.println("Priority B: 0b" + Integer.toBinaryString(priorityB) + " - 0x" + Integer.toHexString(priorityB).toUpperCase());
        System.out.println("HFlip: " + isHFlip() + " VFlip: " + isVFlip() + " LoadNextGraphicsPage: " + isLoadNextGraphicsPage() + " XOffsetHigh " + isXOffsetHigh() + " Priority: " + getPriority() + " Palette?: " + getPalette());
    }

    public boolean isXOffsetHigh() {
        // Some disagreement in documentation if this is if its even or if its just the first bit (priorityA & 0x01) == 0x01
//        return ((priorityA & 0x0F) % 2) == 0;
        return (priorityA & 0x01) == 0x01;
    }

    public SpriteMapEntry setXOffsetHigh(boolean value) {
        if (value) {
            priorityA = (priorityA | 0x01);
        }
        else {
            priorityA = (priorityA & 0xFE);
        }
        return this;
    }

    public boolean is16x16tile() {
//        return (priorityA >> 4) >= 0x8;
        return (priorityA & 0x80) == 0x80;
    }

    public SpriteMapEntry set16x16tile(boolean value) {
        if (value) {
            priorityA = (priorityA | 0x80);
        }
        else {
            priorityA = (priorityA & 0x7F);
        }
        return this;
    }

    public boolean isLoadNextGraphicsPage() {
        return (priorityB & 0x01) == 0x01;
    }

    public SpriteMapEntry setLoadNextGraphicsPage(boolean value) {
        if (value) {
            priorityB = (priorityB | 0x01);
        }
        else {
            priorityB = (priorityB & 0xFE);
        }
        return this;
    }

    private void updateLoadNextGraphicsPage() {
        setLoadNextGraphicsPage(tileIndex >= 0x100);
    }

    public boolean isHFlip() {
        return (priorityB & 0x40) == 0x40;
    }

    public SpriteMapEntry setHFlip(boolean value) {
        if (value) {
            priorityB = (priorityB | 0x40);
        }
        else {
            priorityB = (priorityB & 0xBF);
        }
        return this;
    }

    public boolean isVFlip() {
        return (priorityB & 0x80) == 0x80;
    }

    public SpriteMapEntry setVFlip(boolean value) {
        if (value) {
            priorityB = (priorityB | 0x80);
        }
        else {
            priorityB = (priorityB & 0x7F);
        }
        return this;
    }

    public int getPriority() {
        return ((priorityB & 0x30) >> 4);
    }

    public int getPalette() {
        // ?
//        return 7-((priorityB & 0x0E) >> 1);
        return 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getPriorityA() {
        return priorityA;
    }

    public void setPriorityA(int priorityA) {
        this.priorityA = priorityA;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
        updateLoadNextGraphicsPage();
    }

    public int getPriorityB() {
        return priorityB;
    }

    public void setPriorityB(int priorityB) {
        this.priorityB = priorityB;
    }

    public int getSize() {
        return 5;
    }
}
