package com.ostsoft.games.jsm.animation;

import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class MultiFrame extends AnimationFrame {
    private List<AnimationFrame> animationFrames;

    public MultiFrame(List<AnimationFrame> animationFrames) {
        this.animationFrames = animationFrames;
    }


    @Override
    public void save(ByteStream byteStream) {
        System.out.println("[MultiFrame] save dosent work for MultiFrame");
    }

    @Override
    public List<SpriteMapEntry> getSpriteMapEntries() {
        List<SpriteMapEntry> spriteMapEntries = new ArrayList<>();
        for (AnimationFrame animationFrame : animationFrames) {
            spriteMapEntries.addAll(animationFrame.getSpriteMapEntries());
        }
        return spriteMapEntries;
    }

    @Override
    public void setSpriteMapEntries(List<SpriteMapEntry> spriteMapEntries) {
        System.out.println("[MultiFrame] setSpriteMapEntries dosent work for MultiFrame");
    }

   /* @Override
    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (AnimationFrame animationFrame : animationFrames) {
            tiles.addAll(animationFrame.getTiles());
        }
        return tiles;
    }*/

}
