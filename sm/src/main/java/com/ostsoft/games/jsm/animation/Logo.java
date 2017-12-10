package com.ostsoft.games.jsm.animation;

import com.ostsoft.games.jsm.animation.simple.SimpleAnimationFrame;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import java.util.ArrayList;

public class Logo extends Animation {
    private final String name;
    private final int tilePointer;
    private final int spriteMapPointer;

    public Logo(String name, int tilePointer, int spriteMapPointer) {
        this.name = name;
        this.tilePointer = tilePointer;
        this.spriteMapPointer = spriteMapPointer;
    }

    public void load(ByteStream stream) {
        byte[] tiles = CompressUtil.decompress(stream, BitHelper.snesToPc(tilePointer));
        stream.setPosition(BitHelper.snesToPc(spriteMapPointer));
        animationFrames = new ArrayList<>();
        animationFrames.add(new SimpleAnimationFrame(stream, tiles));
    }

    public void save(ByteStream stream) {
        if (animationFrames.size() > 0) {
            stream.setPosition(BitHelper.snesToPc(spriteMapPointer));
            animationFrames.get(0).save(stream);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
