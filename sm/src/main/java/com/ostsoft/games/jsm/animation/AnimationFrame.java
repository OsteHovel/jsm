package com.ostsoft.games.jsm.animation;

import com.ostsoft.games.jsm.common.SimpleTile;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class AnimationFrame {
    private final List<Tile> tiles = new ArrayList<>();
    protected List<SpriteMapEntry> spriteMapEntries;
    protected int frameLength;

    protected static List<SpriteMapEntry> readSpriteEntries(ByteStream stream) {
        return readSpriteEntries(stream, stream.readReversedUnsignedShort());
    }

    protected static List<SpriteMapEntry> readSpriteEntries(ByteStream stream, int length) {
        List<SpriteMapEntry> spriteMapEntries = new ArrayList<>(length);
        for (int j = 0; j < length; j++) {
            SpriteMapEntry spriteMapEntry = new SpriteMapEntry();
            spriteMapEntry.load(stream);
            spriteMapEntries.add(spriteMapEntry);
        }

        return spriteMapEntries;
    }

    public BufferedImage getImage(List<DnDColor> colors) {
        return ImageUtil.generateImageFromSprites(getSpriteMapEntries(), getTilesBytes(), colors);
    }

    public List<SpriteMapEntry> getSpriteMapEntries() {
        return spriteMapEntries;
    }

    public void setSpriteMapEntries(List<SpriteMapEntry> spriteMapEntries) {
        this.spriteMapEntries = spriteMapEntries;
    }

    public byte[] getTilesBytes() {
        byte[] tilesBytes = new byte[32 * tiles.size()];
        int i = 0;
        for (Tile tile : tiles) {
            System.arraycopy(tile.getBytes(), 0, tilesBytes, i * 32, 32);
            i++;
        }
        return tilesBytes;
    }

    @Deprecated
    public void setTilesBytes(byte[] tilesBytes) {
        tiles.clear();
        for (int i = 0; i < tilesBytes.length / 32; i++) {
            byte[] tileBytes = new byte[32];
            System.arraycopy(tilesBytes, i * 32, tileBytes, 0, tileBytes.length);
            tiles.add(new SimpleTile(tileBytes));
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public abstract void save(ByteStream byteStream);

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = Math.max(Math.min(frameLength, 255), 0);
    }
}
