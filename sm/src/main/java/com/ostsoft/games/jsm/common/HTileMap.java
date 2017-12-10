package com.ostsoft.games.jsm.common;

import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HTileMap extends TileMap implements Storable, ImageWithPalette {
    private int offset;
    private int size;
    private int sizeX;
    private List<Tile> tiles;
    private boolean compressed;

    public HTileMap(int offset, int size, int sizeX, List<Tile> tiles) {
        this.offset = offset;
        this.size = size;
        this.sizeX = sizeX;
        this.tiles = tiles;
        this.compressed = false;
    }

    public HTileMap(int offset, int sizeX, List<Tile> tiles) {
        this.offset = offset;
        this.sizeX = sizeX;
        this.tiles = tiles;
        this.compressed = true;
    }

    @Override
    public void load(ByteStream stream) {
        byte[] tileMap;
        if (compressed) {
            tileMap = CompressUtil.decompress(stream, BitHelper.snesToPc(offset));
        }
        else {
            stream.setPosition(BitHelper.snesToPc(offset));
            tileMap = new byte[size];
            stream.read(tileMap);
        }

        List<Tile> tileList = new ArrayList<>();

        int numberOfTiles = tileMap.length / 8;
        for (int index = 0; index < numberOfTiles; index++) {
            for (int j = 0; j < 4; j++) {
                int i2 = (index * 8) + (j * 2);
                int i1 = (index * 8) + ((j * 2) + 1);

                int tileTableShort = (Byte.toUnsignedInt(tileMap[i1]) << 8) | Byte.toUnsignedInt(tileMap[i2]);
//                System.out.println(Integer.toHexString(tileTableShort));
                boolean flipX = (tileTableShort & 0x4000) > 0;
                boolean flipY = (tileTableShort & 0x8000) > 0;
                boolean priority = (tileTableShort & 0x2000) > 0;

                int tileIndex = tileTableShort & 0x3FF;
                int paletteIndex = (tileTableShort & 0x1C00) >> 10;

                if (tileIndex < tiles.size()) {
                    Tile e = tiles.get(tileIndex);
                    if (e instanceof SimpleTile) {
                        tileList.add(new AttTile((SimpleTile) e, paletteIndex, flipX, flipY));
                    }
                }
            }

        }

        setTiles(tileList);
    }

    @Override
    public void save(ByteStream stream) {

    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Image getImage(Palette palette) {
        BufferedImage image = new BufferedImage(8 * sizeX, (getTiles().size() / sizeX) * 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        int x = 0;
        int y = 0;
        for (Tile tile : getTiles()) {
            if (tile instanceof ImageSupport) {
                g.drawImage(((ImageSupport) tile).getImage(), x, y, x + 8, y + 8, 0, 0, 8, 8, null);
            }
            else if (tile instanceof AttTile) {
                g.drawImage(((AttTile) tile).getImage(palette), x, y, x + 8, y + 8, 0, 0, 8, 8, null);
            }

            x += 8;
            if (x >= 8 * sizeX) {
                x = 0;
                y += 8;
            }
        }
        g.dispose();
        return image;
    }

    public int getSizeX() {
        return sizeX;
    }
}
