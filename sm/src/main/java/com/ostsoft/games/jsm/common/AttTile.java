package com.ostsoft.games.jsm.common;

import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ImageUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class AttTile extends Tile implements ImageWithPalette {
    private Tile tile;
    private int bpp;
    private int paletteIndex;
    private boolean flipX;
    private boolean flipY;

    private Image cachedImage = null;
    private Palette cachedPalette = null;

    public AttTile(Tile tile, int bpp, int paletteIndex, boolean flipX, boolean flipY) {
        this.tile = tile;
        this.bpp = bpp;
        this.paletteIndex = paletteIndex;
        this.flipX = flipX;
        this.flipY = flipY;
    }

    public AttTile(SimpleTile simpleTile, int paletteIndex, boolean flipX, boolean flipY) {
        this.tile = simpleTile;
        this.bpp = simpleTile.getBpp();
        this.paletteIndex = paletteIndex;
        this.flipX = flipX;
        this.flipY = flipY;
    }

    @Override
    public Image getImage(Palette palette) {
        if (this.cachedImage == null || this.cachedPalette != palette) {
            BufferedImage image = ImageUtil.getImageFromTile(tile.getBytes(), 0, palette.getColors(), paletteIndex, bpp, true);
            AffineTransform tx = AffineTransform.getScaleInstance(flipX ? -1 : 1, flipY ? -1 : 1);
            tx.translate(flipX ? -image.getWidth() : 0, flipY ? -image.getHeight() : 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            this.cachedImage = op.filter(image, null);
            this.cachedPalette = palette;
        }
        return this.cachedImage;
    }

    @Override
    public byte[] getBytes() {
        return tile.getBytes();
    }

    @Override
    public void setBytes(byte[] tile) {
        cachedImage = null;
        cachedPalette = null;
        super.setBytes(tile);
    }

    public int getBpp() {
        return bpp;
    }

    public void setBpp(int bpp) {
        cachedImage = null;
        this.bpp = bpp;
    }

    public int getPaletteIndex() {
        return paletteIndex;
    }

    public void setPaletteIndex(int paletteIndex) {
        cachedImage = null;
        this.paletteIndex = paletteIndex;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        cachedImage = null;
        this.flipX = flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        cachedImage = null;
        this.flipY = flipY;
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public AttTile clone() {
        return new AttTile(tile, bpp, paletteIndex, flipX, flipY);
    }

}
