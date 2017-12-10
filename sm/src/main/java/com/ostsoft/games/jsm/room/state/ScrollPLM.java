package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;

public class ScrollPLM {
    public int screen;
    public int scroll;

    public ScrollPLM(ByteStream stream) {
        screen = stream.readUnsignedByte();
        scroll = stream.readUnsignedByte();
    }

    public void printDebug() {
        System.out.println("[ScrollPLM] Screen: " + screen + " Scroll: " + scroll);
    }
}
