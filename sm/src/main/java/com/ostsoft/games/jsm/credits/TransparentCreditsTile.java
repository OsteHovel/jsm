package com.ostsoft.games.jsm.credits;

import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ImageUtil;

import java.awt.*;

public class TransparentCreditsTile extends CreditsTile {

    public TransparentCreditsTile(byte[] bytes, Palette palette, int paletteIndex) {
        super(bytes, palette, paletteIndex);
    }

    @Override
    public Image getImage() {
        return ImageUtil.getTransparent8x8();
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public TransparentCreditsTile clone() {
        return new TransparentCreditsTile(getBytes(), getPalette(), getPaletteIndex());
    }


}
