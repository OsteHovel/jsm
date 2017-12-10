package com.ostsoft.games.jsm.animation.enemy;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.enemy.Enemy;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.EnemyAnimationFrame;
import com.ostsoft.games.jsm.util.ThreeByte;

import java.util.ArrayList;
import java.util.List;

public class EnemyAnimation extends Animation {
    private Enemy enemy;

    public EnemyAnimation(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void load(ByteStream stream) {
        // Read out tile data first
        byte[] tiles = new byte[0x100 * 32 + enemy.tilesLength];
        if (enemy.tilesLength > 0x8000) {
//            System.out.println(enemy.tilesLength + " = " + Integer.toHexString(enemy.getOffset()).toUpperCase());
        }
        else {
            stream.setPosition(BitHelper.snesToPc(enemy.tilePointer));
            stream.read(tiles, 0x100 * 32, enemy.tilesLength);
        }


        // Then read out the spriteMapEntry & frames
        List<AnimationGFXFrame> animationGFXFrames = new ArrayList<>();
        if (EnemyAnimationFrame.hasEnemy(enemy.offset & 0xFFFF)) {
            List<Integer> animationFramesLocations = EnemyAnimationFrame.getAnimationFrames(enemy.offset & 0xFFFF);
            for (Integer animationFramesLocation : animationFramesLocations) {
                stream.setPosition(animationFramesLocation);
                AnimationGFXFrame animationGFXFrame = new AnimationGFXFrame();
                animationGFXFrame.load(stream);
                animationGFXFrames.add(animationGFXFrame);
            }
        }
        else {
            // We got to find them ourselves
            animationGFXFrames = findAnimationFrames(stream, enemy);
        }

        for (AnimationGFXFrame animationGFXFrame : animationGFXFrames) {
            stream.setPosition(BitHelper.threeByteToOffset(new ThreeByte(animationGFXFrame.gfxPointer, enemy.bankPointer)));
            animationGFXFrame.setSpriteMapEntries(readSpriteEntries(stream));
            animationGFXFrame.setTilesBytes(tiles);
        }


        animationFrames = new ArrayList<>();
        animationFrames.addAll(animationGFXFrames);
    }

    protected List<AnimationGFXFrame> findAnimationFrames(ByteStream stream, Enemy enemy) {
        List<AnimationGFXFrame> animationFrames = new ArrayList<>();

        // Try to find the animationFrames
        int paletteOffset = enemy.palettePointer;
        stream.setPosition(BitHelper.snesToPc(paletteOffset));

        AnimationGFXFrame animationGFXFrame = new AnimationGFXFrame();
        animationGFXFrame.load(stream);
        int seek = 0;
        int numberOfSprites = 9999;
        while (numberOfSprites > 1000 && seek < 1000) {
            animationGFXFrame.setFrameLength(0xFFFF);
            while ((animationGFXFrame.getFrameLength() > 0x00F0 || animationGFXFrame.gfxPointer < 0x1000) && seek < 1000) {
                animationGFXFrame = new AnimationGFXFrame();
                animationGFXFrame.load(stream);
                seek += 4;
            }
            // Number of sprites
            stream.setPosition(BitHelper.snesToPc(animationGFXFrame.gfxPointer | (enemy.bankPointer << 16)));
            numberOfSprites = stream.readReversedUnsignedShort();

            // Go back to where we where
            stream.setPosition(BitHelper.snesToPc(paletteOffset + seek));
        }

        stream.setPosition(BitHelper.snesToPc(paletteOffset + seek - 4));
        while (animationGFXFrame.getFrameLength() < 0x00FF && animationGFXFrame.gfxPointer > 0x1000) {
            animationFrames.add(animationGFXFrame);
            animationGFXFrame = new AnimationGFXFrame();
            animationGFXFrame.load(stream);
        }

        return animationFrames;
    }

    @Override
    public void save(ByteStream stream) {

    }
}
