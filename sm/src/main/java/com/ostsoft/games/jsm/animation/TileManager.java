package com.ostsoft.games.jsm.animation;

import com.ostsoft.games.jsm.common.Tile;

import java.util.HashMap;
import java.util.Map;

public class TileManager {
    private HashMap<Integer, Tile> tileMap = new HashMap<>();

    public Tile getTile(int offset) {
        return tileMap.get(offset);
    }

    public void putTile(int offset, Tile tile) {
        tileMap.put(offset, tile);
    }

    public int getOffset(Tile tile) {
        for (Map.Entry<Integer, Tile> integerSpriteMapEntryEntry : tileMap.entrySet()) {
            if (tile == integerSpriteMapEntryEntry.getValue()) {
                return integerSpriteMapEntryEntry.getKey();
            }
        }
        return 0;
    }
}
