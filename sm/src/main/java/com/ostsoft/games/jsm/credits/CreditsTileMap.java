package com.ostsoft.games.jsm.credits;

import com.ostsoft.games.jsm.common.ImageSupport;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.common.TileMap;
import com.ostsoft.games.jsm.palette.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class CreditsTileMap extends TileMap implements ImageSupport {

    public CreditsTileMap(List<Tile> tiles, byte[] tileMap, Palette palette, int index) {
        byte[] tileTable = new byte[32 * 2];
        System.arraycopy(tileMap, index, tileTable, 0, tileTable.length);

        List<Tile> tileList = new ArrayList<>();
        for (int i = 0; i < tileTable.length; i += 2) {
            int tileIndex = Byte.toUnsignedInt(tileTable[i]);
            int tilePalette = Byte.toUnsignedInt(tileTable[i + 1]);

            if (tileIndex == 127) {
                // Transparent
                tileList.add(new TransparentCreditsTile(tiles.get(tileIndex).getBytes(), palette, tilePalette));
            }
            else {
                tileList.add(new CreditsTile(tiles.get(tileIndex).getBytes(), palette, tilePalette));
            }
        }

        setTiles(tileList);
    }

    @Override
    public Image getImage() {
        BufferedImage image = new BufferedImage(32 * 8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        int x = 0;
        int y = 0;
        for (Tile tile : getTiles()) {
            if (tile instanceof ImageSupport) {
                g.drawImage(((ImageSupport) tile).getImage(), x, y, x + 8, y + 8, 0, 0, 8, 8, null);
            }
            x += 8;
        }
        g.dispose();
        return image;
    }
}
