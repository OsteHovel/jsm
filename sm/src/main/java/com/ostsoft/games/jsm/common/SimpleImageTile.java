package com.ostsoft.games.jsm.common;

import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ImageUtil;

import java.awt.*;

public class SimpleImageTile extends Tile implements ImageSupport {
    protected Image cachedImage = null;
    private Palette palette;

    public SimpleImageTile(byte[] tileBytes, Palette palette) {
        setBytes(tileBytes);
        this.palette = palette;
    }

    public Image getImage() {
        if (cachedImage == null) {
            cachedImage = ImageUtil.getImageFromTile(getBytes(), 0, palette.getColors(), 0, 4, true);
        }
        return cachedImage;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        cachedImage = null;
        this.palette = palette;
    }

    @Override
    public void setBytes(byte[] tile) {
        cachedImage = null;
        super.setBytes(tile);
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public SimpleImageTile clone() {
        return new SimpleImageTile(getBytes(), palette);
    }
}
