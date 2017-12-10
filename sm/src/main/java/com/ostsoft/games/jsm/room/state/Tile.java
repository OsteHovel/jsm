package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.PatternUtil;

public class Tile {
    public int blockID;
    public int patternByte;

    public Tile(ByteStream stream) {
        int b1 = stream.readUnsignedByte();
        int b2 = stream.readUnsignedByte();
        this.blockID = b1 | ((b2 & 0x3) << 8);
        this.patternByte = b2;
    }

    public void printDebug() {
        System.out.println("BlockID: 0x" + Integer.toHexString(blockID).toUpperCase() + " - " + blockID);
        System.out.println("PatternByte: 0x" + Integer.toHexString(patternByte).toUpperCase() + " - " + patternByte);
        System.out.println("Type: " + getBlockType().name() + " H-Flip: " + isHFlip() + " V-Flip: " + isVFlip());
    }

    public PatternUtil.BlockType getBlockType() {
        return PatternUtil.getBlockType(patternByte);
    }

    public boolean isVFlip() {
        return PatternUtil.isVFlip(patternByte);
    }

    public boolean isHFlip() {
        return PatternUtil.isHFlip(patternByte);
    }

}
