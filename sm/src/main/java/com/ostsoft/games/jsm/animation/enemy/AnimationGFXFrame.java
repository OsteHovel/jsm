package com.ostsoft.games.jsm.animation.enemy;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.util.ByteStream;

public class AnimationGFXFrame extends AnimationFrame {
    public int gfxPointer;

    public void load(ByteStream stream) {
        setFrameLength(stream.readReversedUnsignedShort());
        gfxPointer = stream.readReversedUnsignedShort();
    }


    public void printDebug() {
        System.out.println("AnimationGFXFrame: FrameCount: 0x" + Integer.toHexString(getFrameLength()).toUpperCase() + " - " + getFrameLength() + " gfxPointer: 0x" + Integer.toHexString(gfxPointer).toUpperCase());
    }


    @Override
    public void save(ByteStream byteStream) {

    }
}
