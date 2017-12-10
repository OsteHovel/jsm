package com.ostsoft.games.jsm.graphics;

import java.util.ArrayList;
import java.util.List;

public class TileTableUtil {
    private TileTableUtil() {
    }

    public static List<Block> processTileTable(byte[] tileTable) {
        List<Block> blockList = new ArrayList<>();
        int numberOfTiles = tileTable.length / 2;
        for (int i = 0; i < numberOfTiles; i++) {
            int block = (Byte.toUnsignedInt(tileTable[(i * 2 + 1)]) << 8) | Byte.toUnsignedInt(tileTable[i * 2]);
            blockList.add(new Block(block));
        }
        return blockList;
    }
}
