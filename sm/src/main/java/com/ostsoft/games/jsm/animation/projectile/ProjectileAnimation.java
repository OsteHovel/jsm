package com.ostsoft.games.jsm.animation.projectile;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;

public class ProjectileAnimation extends Animation {
    public int loopPointer;

    public ProjectileAnimation(ByteStream byteStream, byte[] tiles) {
        animationFrames = new ArrayList<>();
        ProjectileFrame projectileFrame = new ProjectileFrame(byteStream, tiles);
        while (projectileFrame.delayInFrames != 0x8239) {
            animationFrames.add(projectileFrame);
            projectileFrame = new ProjectileFrame(byteStream, tiles);
        }
        loopPointer = projectileFrame.spriteCodePointer;

        for (AnimationFrame animationFrame : animationFrames) {
            if (animationFrame instanceof ProjectileFrame) {
                ProjectileFrame projectileFrame1 = (ProjectileFrame) animationFrame;
                byteStream.setPosition(BitHelper.snesToPc(projectileFrame1.spriteCodePointer));
                projectileFrame1.setSpriteMapEntries(readSpriteEntries(byteStream));
            }
        }
    }

    @Override
    public void load(ByteStream stream) {

    }

    @Override
    public void save(ByteStream stream) {

    }
}
