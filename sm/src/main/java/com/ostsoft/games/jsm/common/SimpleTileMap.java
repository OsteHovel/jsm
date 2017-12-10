package com.ostsoft.games.jsm.common;

import com.ostsoft.games.jsm.palette.Palette;

import java.util.ArrayList;

public class SimpleTileMap extends TileMap {
    @Deprecated
    public SimpleTileMap(java.util.List<Tile> tiles, byte[] tileMap, Palette palette, int bpp) {
        java.util.List<Tile> tileList = new ArrayList<>();

        int numberOfTiles = tileMap.length / (bpp * 8);
        for (int index = 0; index < numberOfTiles; index++) {
            for (int j = 0; j < 4; j++) {
                int i2 = (index * 8) + (j * 2);
                int i1 = (index * 8) + ((j * 2) + 1);
                int tileTableShort = (Byte.toUnsignedInt(tileMap[i1]) << 8) | Byte.toUnsignedInt(tileMap[i2]);

                boolean flipX = (tileTableShort & 0x4000) > 0;
                boolean flipY = (tileTableShort & 0x8000) > 0;
                boolean priority = (tileTableShort & 0x2000) > 0;

                int tileIndex = tileTableShort & 0x3FF;
                int tilePalette = (tileTableShort & 0x1C00) >> 10;


                tileList.add(tiles.get(tileIndex));
            }

        }

        setTiles(tileList);
    }

}
