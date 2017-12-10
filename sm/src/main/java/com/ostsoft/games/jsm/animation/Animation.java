package com.ostsoft.games.jsm.animation;

import com.ostsoft.games.jsm.util.ByteStream;

import java.util.List;

public abstract class Animation {
    public List<AnimationFrame> animationFrames;

    protected List<SpriteMapEntry> readSpriteEntries(ByteStream stream) {
        return AnimationFrame.readSpriteEntries(stream, stream.readReversedUnsignedShort());
    }

    public abstract void load(ByteStream stream);

    public abstract void save(ByteStream stream);

    public List<AnimationFrame> getAnimationFrames() {
        return animationFrames;
    }
}
