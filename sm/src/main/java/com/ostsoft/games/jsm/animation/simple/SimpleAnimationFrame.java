package com.ostsoft.games.jsm.animation.simple;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.List;

public class SimpleAnimationFrame extends AnimationFrame {
    public SimpleAnimationFrame(List<SpriteMapEntry> spriteMapEntries, byte[] tiles) {
        this.setSpriteMapEntries(spriteMapEntries);
        this.setTilesBytes(tiles);
    }


    public SimpleAnimationFrame(ByteStream stream, byte[] tiles) {
        this.setSpriteMapEntries(readSpriteEntries(stream));
        this.setTilesBytes(tiles);
    }

    public SimpleAnimationFrame(ByteStream stream, int length, byte[] tiles) {
        this.setSpriteMapEntries(readSpriteEntries(stream, length));
        this.setTilesBytes(tiles);
    }

    @Override
    public void save(ByteStream stream) {
        stream.writeUnsignedReversedShort(spriteMapEntries.size());
        for (SpriteMapEntry spriteMapEntry : spriteMapEntries) {
            spriteMapEntry.save(stream);
        }
    }
}
