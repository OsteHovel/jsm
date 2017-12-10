package com.ostsoft.games.jsm.animation.simple;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnimation extends Animation {

    public SimpleAnimation(ByteStream byteStream, byte[] tiles) {
        this.animationFrames = new ArrayList<>();
        this.animationFrames.add(new SimpleAnimationFrame(byteStream, tiles));
    }

    public SimpleAnimation(ByteStream byteStream, int length, byte[] tiles) {
        this.animationFrames = new ArrayList<>();
        this.animationFrames.add(new SimpleAnimationFrame(byteStream, length, tiles));
    }

    public SimpleAnimation(List<SpriteMapEntry> spriteMapEntries, byte[] tiles) {
        AnimationFrame animationFrame = new SimpleAnimationFrame(spriteMapEntries, tiles);
        this.animationFrames = new ArrayList<>();
        this.animationFrames.add(animationFrame);
    }

    @Override
    public void load(ByteStream stream) {

    }

    @Override
    public void save(ByteStream stream) {
        for (AnimationFrame animationFrame : animationFrames) {
            animationFrame.save(stream);
        }
    }
}
