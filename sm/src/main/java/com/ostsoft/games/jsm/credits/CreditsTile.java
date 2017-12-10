package com.ostsoft.games.jsm.credits;

import com.ostsoft.games.jsm.common.SimpleImageTile;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ImageUtil;

import java.awt.*;

public class CreditsTile extends SimpleImageTile {
    private int paletteIndex;

    public CreditsTile(byte[] bytes, Palette palette, int paletteIndex) {
        super(bytes, palette);
        this.paletteIndex = paletteIndex;
    }

    public CreditsTile(Tile tile, Palette palette) {
        super(tile.getBytes(), palette);
        this.paletteIndex = 0;
    }

    @Override
    public Image getImage() {
        if (cachedImage == null) {
            cachedImage = ImageUtil.getImageFromTile(getBytes(), 0, getPalette().getColors(), paletteIndex / 4, 4, true);
        }
        return cachedImage;
    }


    public int getPaletteIndex() {
        return paletteIndex;
    }

    public void setPaletteIndex(int paletteIndex) {
        cachedImage = null;
        this.paletteIndex = paletteIndex;
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public CreditsTile clone() {
        return new CreditsTile(getBytes(), getPalette(), paletteIndex);
    }
}
