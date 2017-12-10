package com.ostsoft.games.jsm.graphics;

public class Block {
    public int block;

    public Block(int block) {
        this.block = block;
    }

    public boolean isVFlip() {
        return (block & 0x4000) > 0;
    }

    public boolean isHFlip() {
        return (block & 0x4000) > 0;
    }

    public boolean hasPriority() {
        return (block & 0x2000) > 0;
    }

    public int getPalette() {
        return (block & 0x1C00) >> 10;
    }

    public int getIndex() {
        return block & 0x3FF;
    }

}
