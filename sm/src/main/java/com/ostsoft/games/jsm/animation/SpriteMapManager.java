package com.ostsoft.games.jsm.animation;

import java.util.HashMap;
import java.util.Map;

public class SpriteMapManager {
    private HashMap<Integer, SpriteMapEntry> spriteMapEntries = new HashMap<>();

    public SpriteMapEntry getSpriteMapEntry(int offset) {
        return spriteMapEntries.get(offset);
    }

    public void putSpriteMapEntry(int offset, SpriteMapEntry spriteMapEntry) {
        spriteMapEntries.put(offset, spriteMapEntry);
    }

    public int getOffset(SpriteMapEntry spriteMapEntry) {
        for (Map.Entry<Integer, SpriteMapEntry> integerSpriteMapEntryEntry : spriteMapEntries.entrySet()) {
            if (spriteMapEntry == integerSpriteMapEntryEntry.getValue()) {
                return integerSpriteMapEntryEntry.getKey();
            }
        }
        return 0;
    }
}
