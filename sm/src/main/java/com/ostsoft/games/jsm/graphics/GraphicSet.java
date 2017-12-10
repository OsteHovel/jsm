package com.ostsoft.games.jsm.graphics;

import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicSet {
    public byte[] creTiles;
    public byte[] sceneryTiles;
    public byte[] tiles;
    public Palette palette;
    private int index;
    private byte[] tileTable; // Combined creTileTable & sceneryTileTable

    public GraphicSet(ByteStream stream, int index) {
        this.index = index;
        stream.setPosition(BitHelper.snesToPc(0x8FE6A2 + (index * 9)));
        int tablePointer = stream.readReversedUnsigned3Bytes();
        int tilePointer = stream.readReversedUnsigned3Bytes();
        int palettePointer = stream.readReversedUnsigned3Bytes();

        // Decode Palette
        palette = new Palette("GraphicSet palette " + index, palettePointer, true, 0);
        palette.load(stream);

        // Decode tileTables and combine them
        byte[] creTileTable = CompressUtil.decompress(stream, BitHelper.snesToPc(0xB9A09D));
        byte[] sceneryTileTable = CompressUtil.decompress(stream, BitHelper.snesToPc(tablePointer));
        tileTable = new byte[creTileTable.length + sceneryTileTable.length];
        System.arraycopy(creTileTable, 0, tileTable, 0, creTileTable.length);
        System.arraycopy(sceneryTileTable, 0, tileTable, creTileTable.length, sceneryTileTable.length);

        // Decompress creTiles
        // Default CRE address is: 0x1C8000
        // CRE address for Ceres is: 0x203004
        creTiles = CompressUtil.decompress(stream, 0x1C8000);

        // Decompress sceneryTiles
        sceneryTiles = CompressUtil.decompress(stream, BitHelper.snesToPc(tilePointer));

        tiles = new byte[(0x280 * 32) + creTiles.length];
        System.arraycopy(sceneryTiles, 0, tiles, 0, sceneryTiles.length);
        System.arraycopy(creTiles, 0, tiles, (0x280 * 32), creTiles.length);
    }

    public int getNumberOfTiles() {
        return tileTable.length / 8;
    }

    public BufferedImage generateImageFromTileTable(int tilesInWidth, PriorityType priorityType) {
        int numberOfTiles = tileTable.length / 8;
        int imageWidth = 16 * tilesInWidth;
        int imageHeight = (numberOfTiles / tilesInWidth) * 16;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, imageWidth, imageHeight);

        for (int index = 0; index < numberOfTiles; index++) {
            for (int j = 0; j < 4; j++) {
                int i2 = (index * 8) + (j * 2);
                int i1 = (index * 8) + ((j * 2) + 1);
                int tileTableShort = (Byte.toUnsignedInt(tileTable[i1]) << 8) | Byte.toUnsignedInt(tileTable[i2]);

                boolean flipX = (tileTableShort & 0x4000) > 0;
                boolean flipY = (tileTableShort & 0x8000) > 0;
                boolean priority = (tileTableShort & 0x2000) > 0;

                int tileIndex = tileTableShort & 0x3FF;
                int tilePalette = (tileTableShort & 0x1C00) >> 10;

                BufferedImage tileImage;
                if ((priorityType == PriorityType.FRONT_ONLY && !priority) || (priorityType == PriorityType.BACK_ONLY && priority)) {
                    tileImage = ImageUtil.getTransparent8x8();
                }
                else if (tileIndex >= 0 && tileIndex < tiles.length / 32) {
                    tileImage = ImageUtil.getImageFromTile(tiles, tileIndex, palette.getColors(), tilePalette, 4, true);
                }
                else {
                    tileImage = ImageUtil.get8x8(Color.MAGENTA);
                    System.out.println("[GraphicSet] generateImageFromTileTable(): Missing tileIndex: " + tileIndex);
                }
                int stepY = (index / tilesInWidth);
                int x = ((index - (stepY * tilesInWidth)) * 16) + (j % 2 * 8);
                int y = (stepY * 16) + ((3 - j) > 1 ? 0 : 8);

                if (flipX && flipY) {
                    g.drawImage(tileImage, x, y, x + 8, y + 8, 8, 8, 0, 0, null);
                }
                else if (flipX) {
                    g.drawImage(tileImage, x, y, x + 8, y + 8, 8, 0, 0, 8, null);
                }
                else if (flipY) {
                    g.drawImage(tileImage, x, y, x + 8, y + 8, 0, 8, 8, 0, null);
                }
                else {
                    g.drawImage(tileImage, x, y, x + 8, y + 8, 0, 0, 8, 8, null);
                }
            }

        }

        return image;
    }

    public BufferedImage generateImageFromTileTable8x8(byte[] tileTable, int tilesInWidth, boolean onlyPriority) {
        int numberOfTiles = tileTable.length / 2;
        int imageWidth = 8 * tilesInWidth;
        int imageHeight = (numberOfTiles / tilesInWidth) * 8;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, imageWidth, imageHeight);
        for (int index = 0; index < numberOfTiles; index++) {
            int i2 = (index * 2);
            int i1 = (index * 2) + 1;
            int tileTableShort = (Byte.toUnsignedInt(tileTable[i1]) << 8) | Byte.toUnsignedInt(tileTable[i2]);

            boolean flipX = (tileTableShort & 0x4000) > 0;
            boolean flipY = (tileTableShort & 0x8000) > 0;
            boolean priority = (tileTableShort & 0x2000) > 0;

            int tileIndex = tileTableShort & 0x3FF;
            int tilePalette = (tileTableShort & 0x1C00) >> 10;

            BufferedImage tileImage;
            if (onlyPriority && !priority) {
                tileImage = ImageUtil.getTransparent8x8();
            }
            else if (tileIndex >= 0 && tileIndex < sceneryTiles.length / 32) {
                tileImage = ImageUtil.getImageFromTile(sceneryTiles, tileIndex, palette.getColors(), tilePalette, 4, true);
            }
            else if (tileIndex >= 0x280 && tileIndex - 0x280 < creTiles.length / 32) {
                tileImage = ImageUtil.getImageFromTile(creTiles, tileIndex - 0x280, palette.getColors(), tilePalette, 4, true);
            }
            else {
                System.out.println("[GraphicSet] generateImageFromTileTable8x8(): Missing tileIndex: " + tileIndex);
                tileImage = ImageUtil.get8x8(Color.MAGENTA);
            }
            int stepY = (index / tilesInWidth);
            int x = ((index - (stepY * tilesInWidth)) * 8);
            int y = (stepY * 8);

            if (flipX && flipY) {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 8, 8, 0, 0, null);
            }
            else if (flipX) {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 8, 0, 0, 8, null);
            }
            else if (flipY) {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 0, 8, 8, 0, null);
            }
            else {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 0, 0, 8, 8, null);
            }
        }

        return image;
    }


    public void printDebug() {
        JFrame jframe = new JFrame() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                BufferedImage creImage = ImageUtil.getImageFromTiles(creTiles, palette.getColors(), 0, 16, 4);
                BufferedImage tilesImage = ImageUtil.getImageFromTiles(sceneryTiles, palette.getColors(), 0, 16, 4);

                for (int i = 0; i < palette.getColors().size(); i++) {
                    Color color = palette.getColors().get(i);
                    g.setColor(color);
                    g.fillRect(10 + (i * 5), 30, 10, 20);
                }
                g.drawImage(tilesImage, 10, 60, tilesImage.getWidth() * 2, tilesImage.getHeight() * 2, null);
                g.drawImage(creImage, 286, 60, creImage.getWidth() * 2, creImage.getHeight() * 2, null);

                g.drawImage(generateImageFromTileTable(32, PriorityType.BOTH), 550, 60, null);
            }
        };
        jframe.setVisible(true);
        jframe.setSize(1024, 1200);

        System.out.println("Number of creTiles: " + creTiles.length / 32);
        System.out.println("Number of sceneryTiles: " + (sceneryTiles.length / 32));
    }

    public int getIndex() {
        return index;
    }

    public boolean isPriorityTile(int tileID) {
        boolean priority = false;
        for (int j = 0; j < 4; j++) {
            int offsetByte2 = (tileID * 8) + (j * 2);
            int offsetByte1 = (tileID * 8) + ((j * 2) + 1);
            int attributes = (Byte.toUnsignedInt(tileTable[offsetByte1]) << 8) | Byte.toUnsignedInt(tileTable[offsetByte2]);

            if ((attributes & 0x2000) > 0) {
                priority = true;
            }
        }
        return priority;
    }

    public BufferedImage generateImageFromTile(int index, PriorityType priorityType, int bpp) {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, 16, 16);

        for (int j = 0; j < 4; j++) {
            int i2 = (index * 8) + (j * 2);
            int i1 = (index * 8) + ((j * 2) + 1);
            int tileTableShort = (Byte.toUnsignedInt(tileTable[i1]) << 8) | Byte.toUnsignedInt(tileTable[i2]);

            boolean flipX = (tileTableShort & 0x4000) > 0;
            boolean flipY = (tileTableShort & 0x8000) > 0;
            boolean priority = (tileTableShort & 0x2000) > 0;

            int tileIndex = tileTableShort & 0x3FF;
            int tilePalette = (tileTableShort & 0x1C00) >> 10;

            BufferedImage tileImage;
            if ((priorityType == PriorityType.FRONT_ONLY && !priority) || (priorityType == PriorityType.BACK_ONLY && priority)) {
                tileImage = ImageUtil.getTransparent8x8();
            }
            else if (tileIndex >= 0 && tileIndex < tiles.length / (8 * bpp)) {
                tileImage = ImageUtil.getImageFromTile(tiles, tileIndex, palette.getColors(), tilePalette, bpp, true);
            }
            else {
                tileImage = ImageUtil.get8x8(Color.MAGENTA);
                System.out.println("[GraphicSet] generateImageFromTile(): Missing tileIndex: " + tileIndex);
            }
            int x = (j % 2 * 8);
            int y = ((3 - j) > 1 ? 0 : 8);

            if (flipX && flipY) {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 8, 8, 0, 0, null);
            }
            else if (flipX) {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 8, 0, 0, 8, null);
            }
            else if (flipY) {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 0, 8, 8, 0, null);
            }
            else {
                g.drawImage(tileImage, x, y, x + 8, y + 8, 0, 0, 8, 8, null);
            }
        }

        return image;
    }


}
