package com.ostsoft.games.jsm.animation.projectile;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.util.ByteStream;

public class ProjectileFrame extends AnimationFrame {
    public int delayInFrames;
    public int spriteCodePointer;
    public int xRadius; // To get it in width, multiply by 2
    public int yRadius; // To get it in height, multiply by 2
    public int entryNumber;

    public ProjectileFrame(ByteStream stream, byte[] tiles) {
        delayInFrames = stream.readReversedUnsignedShort();
        spriteCodePointer = stream.readReversedUnsignedShort() | (0x93 << 16);
        xRadius = stream.readUnsignedByte();
        yRadius = stream.readUnsignedByte();
        entryNumber = stream.readReversedUnsignedShort();

        this.setTilesBytes(tiles);
    }

    @Override
    public void save(ByteStream byteStream) {

    }
}
