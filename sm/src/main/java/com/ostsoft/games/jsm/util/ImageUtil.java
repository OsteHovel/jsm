package com.ostsoft.games.jsm.util;

import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.graphics.PriorityType;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageUtil {
    private static BufferedImage transparent8x8 = null;

    private ImageUtil() {
    }

    public static BufferedImage generateImageFromSprites(List<SpriteMapEntry> sprites, byte[] tiles, List<DnDColor> colors) {
        int maxX = 0;
        int maxY = 0;
        int minX = 0;
        int minY = 0;

        for (SpriteMapEntry sprite : sprites) {
            if (sprite.is16x16tile()) {
                maxX = Math.max(maxX, sprite.getX() + 16);
                maxY = Math.max(maxY, sprite.getY() + 16);
            }
            else {
                maxX = Math.max(maxX, sprite.getX() + 8);
                maxY = Math.max(maxY, sprite.getY() + 8);
            }
            minX = Math.min(minX, sprite.getX());
            minY = Math.min(minY, sprite.getY());
        }

        int imageWidth = Math.max((0 - minX), maxX) * 2;
        int imageHeight = Math.max((0 - minY), maxY) * 2;

        if (imageWidth < 64) {
            imageWidth = 64;
        }
        if (imageHeight < 64) {
            imageHeight = 64;
        }


        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, imageWidth, imageHeight);

//        g.setColor(Color.MAGENTA);
//        g.drawLine(imageWidth / 2, 0, imageWidth / 2, imageHeight);
//        g.drawLine(0, imageHeight/2, imageWidth, imageHeight/2);

        for (int index = 0; index < sprites.size(); index++) {
            SpriteMapEntry spriteMapEntry = sprites.get(index);

            int x = (imageWidth / 2) + spriteMapEntry.getX();
            int y = (imageHeight / 2) + spriteMapEntry.getY();

//            System.out.println(spriteMapEntry.getPalette());

            int tileIndex = spriteMapEntry.getTileIndex();
            if (spriteMapEntry.is16x16tile()) {
                for (int yG = 0; yG < 2; yG++) {
                    for (int xG = 0; xG < 2; xG++) {
                        int xC = x + (xG * 8);
                        int yC = y + (yG * 8);

                        if (spriteMapEntry.isHFlip()) {
                            xC = x + ((1 - xG) * 8);
                        }
                        if (spriteMapEntry.isVFlip()) {
                            yC = y + ((1 - yG) * 8);
                        }

//                        if (spriteMapEntry.isXOffsetHigh()>0) {
//                            xC = xC + spriteMapEntry.isXOffsetHigh();
//                        }

                        int modifiedTileIndex = tileIndex + ((yG * 0x10) + xG);
                        drawTile(tiles, colors, spriteMapEntry.getPalette(), g, modifiedTileIndex, xC, yC, spriteMapEntry.isHFlip(), spriteMapEntry.isVFlip());
                    }
                }
            }
            else {
                drawTile(tiles, colors, spriteMapEntry.getPalette(), g, tileIndex, x, y, spriteMapEntry.isHFlip(), spriteMapEntry.isVFlip());
            }
        }

        return image;
    }

    protected static void drawTile(byte[] tiles, List<DnDColor> colors, int palette, Graphics2D g, int index, int x, int y, boolean flipX, boolean flipY) {
        BufferedImage tileImage;
        if (index < tiles.length / 32 && index >= 0) {
            tileImage = ImageUtil.getImageFromTile(tiles, index, colors, palette, 4, true);
        }
        else if (index < 0) {
            tileImage = get8x8(Color.GREEN);
        }
        else {
            tileImage = get8x8(Color.MAGENTA);
//            System.out.println("Missing index: " + index);
        }
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

    public static BufferedImage generateImageFromTileTable(byte[] tileTable, byte[] tiles, Palette palette, int tilesInWidth, PriorityType priorityType, int bpp) {
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
                else if (tileIndex >= 0 && tileIndex < tiles.length / (bpp * 16)) {
                    tileImage = ImageUtil.getImageFromTile(tiles, tileIndex, palette.getColors(), tilePalette, bpp, true);
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


    public static BufferedImage generateImageFromTileTable8x8(byte[] tileTable, byte[] tiles, int tilesInWidth, boolean onlyPriority, Palette palette, int bpp) {
        int numberOfTiles = tileTable.length / 2;
        int imageWidth = 8 * tilesInWidth;
        int imageHeight = (numberOfTiles / tilesInWidth) * 8;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, imageWidth, imageHeight);
        for (int index = 0; index < numberOfTiles; index++) {
            int i2 = (index * 2);
            byte b1 = tileTable[(i2)];
            int i1 = (index * 2) + 1;
            byte b2 = tileTable[(i1)];
            int tileTableShort = (Byte.toUnsignedInt(b2) << 8) | Byte.toUnsignedInt(b1);

            boolean flipX = (tileTableShort & 0x4000) > 0;
            boolean flipY = (tileTableShort & 0x8000) > 0;
            boolean priority = (tileTableShort & 0x2000) > 0;

            int tileIndex = tileTableShort & 0x3FF;
            //int tilePalette = (tileTableShort & 0x1C00) >> 10;
            int tilePalette = 0;

            BufferedImage tileImage;
            if (onlyPriority && !priority) {
                tileImage = ImageUtil.getTransparent8x8();
            }
            else if (tileIndex < tiles.length / (8 * bpp)) {
                tileImage = ImageUtil.getImageFromTile(tiles, tileIndex, palette.getColors(), tilePalette, bpp, true);
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

    public static BufferedImage getImageFromTiles(byte[] tiles, List<DnDColor> colors, int paletteIndex, int maxX, int bpp) {
        int numberOfTiles = (tiles.length / (8 * bpp));

        int maxY = numberOfTiles / maxX;
        if (maxY == 0) {
            maxX = numberOfTiles;
            maxY = 1;
        }

        if (maxX == 0) {
            return get8x8(Color.red);
        }

        BufferedImage image = new BufferedImage(maxX * 16, maxY * 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, 128, maxY * 16);

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                g.drawImage(getImageFromTile(tiles, (y * maxX) + x, colors, paletteIndex, bpp, true), x * 8, y * 8, null);
            }
        }
        g.dispose();
        return image;
    }

    public static BufferedImage getImageFromTile(byte[] tiles, int index, List<DnDColor> colors, int palette, int bpp, boolean interleaved) {
        BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, 8, 8);

        int[][] tile = getTile(tiles, index, bpp, interleaved);
        for (int xTile = 0; xTile < 8; xTile++) {
            for (int yTile = 0; yTile < 8; yTile++) {
                int colorIndex = tile[xTile][yTile];
                int paletteCompensatedColorIndex = (palette * 16) + colorIndex;
                if (paletteCompensatedColorIndex < colors.size()) {
                    if (paletteCompensatedColorIndex % 16 == 0) {
                        g.setColor(new Color(0, 0, 0, 0));
                    }
                    else {
                        g.setColor(colors.get(paletteCompensatedColorIndex));
                    }
                }
                else {
                    g.setColor(Color.MAGENTA);
                }

                g.fillRect(xTile, yTile, 1, 1);
            }
        }
        g.dispose();
        return image;
    }

    public static void drawTile2(Graphics2D g, byte[] tiles, int index, List<Color> colors, int palette, int bpp) {
        int[][] tile = getTile(tiles, index, bpp, false);
        for (int xTile = 0; xTile < 8; xTile++) {
            for (int yTile = 0; yTile < 8; yTile++) {
                int colorIndex = tile[xTile][yTile];
                int paletteCompensatedColorIndex = (palette * 16) + colorIndex;
                if (paletteCompensatedColorIndex < colors.size()) {
                    g.setColor(colors.get(paletteCompensatedColorIndex));
                }
                else {
                    g.setColor(Color.MAGENTA);
                }
                g.fillRect(xTile, yTile, 1, 1);
            }
        }
    }

    public static int[][] getTile(byte[] tiles, int index, int bpp, boolean interleaved) {
        int[][] tile = new int[8][8];
        if (bpp < 8) { // Normal
            for (int bitPlane = 0; bitPlane < bpp / 2; bitPlane++) {
                for (int row = 0; row < 8; row++) {
                    int offset = (index * (8 * bpp)) + (bitPlane * 16) + (row * 2);
                    byte byte1 = tiles[offset];
                    byte byte2 = tiles[offset + 1];
                    for (int k = 0; k < 8; k++) {
                        tile[k][row] = tile[k][row] | (((byte1 >> 7 - k) & 0x1) << (bitPlane * 2));
                    }
                    for (int k = 0; k < 8; k++) {
                        tile[k][row] = tile[k][row] | (((byte2 >> 7 - k) & 0x1) << ((bitPlane * 2) + 1));
                    }
                }
            }
        }
        else if (bpp == 8) { // Mode 7
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    int offset = ((index * (8 * bpp)) + (y * 8) + x);
                    if (interleaved) {
                        offset = offset * 2;
                        offset++;
                    }
                    if (offset < tiles.length) {
                        tile[x][y] = Byte.toUnsignedInt(tiles[offset]);
                    }
                    else {
                        tile[x][y] = Integer.MAX_VALUE;
                    }

                }
            }
        }
        else {
            System.out.println("[ImageUtil] Unsupported BPP");
        }

        return tile;
    }

    public static byte[] getBytes(int[][] ints, int bpp) {
        byte[] bytes = new byte[8 * bpp];
        for (int bitPlane = 0; bitPlane < bpp / 2; bitPlane++) {
            for (int row = 0; row < 8; row++) {
                int offset = (bitPlane * 16) + (row * 2);
                byte byte1 = 0;
                byte byte2 = 0;

                for (int k = 0; k < 8; k++) {
                    byte1 = (byte) (byte1 | (((ints[k][row] >> (bitPlane * 2)) & 0x1) << 7 - k));
//                    tile[k][row] = tile[k][row] | (((byte1 >> 7 - k) & 0x1) << (bitPlane * 2));
                }
                for (int k = 0; k < 8; k++) {
                    byte2 = (byte) (byte2 | (((ints[k][row] >> ((bitPlane * 2) + 1)) & 0x1) << (7 - k)));
//                    tile[k][row] = tile[k][row] | (((byte2 >> 7 - k) & 0x1) << ((bitPlane * 2) + 1));
                }
                bytes[offset] = byte1;
                bytes[offset + 1] = byte2;
            }
        }
        return bytes;
    }

    public static BufferedImage getTransparent8x8() {
        if (transparent8x8 == null) {
            transparent8x8 = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) transparent8x8.getGraphics();
            g2.setColor(new Color(0, 0, 0, 0));
            g2.fillRect(0, 0, 8, 8);
            g2.dispose();
        }
        return transparent8x8;
    }

    public static BufferedImage get8x8(Color color) {
        BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, 8, 8);
        g2.dispose();
        return image;
    }
}
