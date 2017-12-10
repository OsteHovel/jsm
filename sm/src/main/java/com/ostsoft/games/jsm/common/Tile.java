package com.ostsoft.games.jsm.common;


public abstract class Tile implements DnD, Cloneable {
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public abstract Object clone();

}
