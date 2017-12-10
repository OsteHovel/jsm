package com.ostsoft.games.jsm.animation;

import java.util.HashMap;
import java.util.Map;

public class FrameManager {
    private HashMap<Integer, AnimationFrame> frameMap = new HashMap<>();

    public AnimationFrame getAnimationFrame(int offset) {
        return frameMap.get(offset);
    }

    public void putAnimationFrame(int offset, AnimationFrame animationFrame) {
        frameMap.put(offset, animationFrame);
    }

    public int getOffset(AnimationFrame animationFrame) {
        for (Map.Entry<Integer, AnimationFrame> animationFrameEntry : frameMap.entrySet()) {
            if (animationFrame == animationFrameEntry.getValue()) {
                return animationFrameEntry.getKey();
            }
        }
        return 0;
    }
}
