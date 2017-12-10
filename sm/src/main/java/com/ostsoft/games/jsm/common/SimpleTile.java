package com.ostsoft.games.jsm.common;

import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ImageUtil;

import java.awt.*;

public class SimpleTile extends Tile implements ImageWithPalette {
    private final int bpp;
    private Image cachedImage = null;
    private Palette palette = null;

    public SimpleTile(byte[] tileBytes) {
        this.bpp = 4;
        setBytes(tileBytes);
    }

    public SimpleTile(byte[] tileBytes, int bpp) {
        this.bpp = bpp;
        setBytes(tileBytes);
    }

    @Override
    public Image getImage(Palette palette) {
        if (cachedImage == null || this.palette != palette) {
            cachedImage = ImageUtil.getImageFromTile(getBytes(), 0, palette.getColors(), 0, bpp, true);
            this.palette = palette;
        }
        return cachedImage;
    }

    @Override
    public void setBytes(byte[] tile) {
        cachedImage = null;
        palette = null;
        super.setBytes(tile);
    }

    public int getBpp() {
        return bpp;
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public SimpleTile clone() {
        return new SimpleTile(getBytes());
    }

}
