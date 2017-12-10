package com.ostsoft.games.jsm.palette;

import com.ostsoft.games.jsm.common.DnD;

import java.awt.*;
import java.awt.color.ColorSpace;

public class DnDColor extends Color implements DnD {
    public DnDColor(int r, int g, int b) {
        super(r, g, b);
    }

    public DnDColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public DnDColor(int rgb) {
        super(rgb);
    }

    public DnDColor(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public DnDColor(float r, float g, float b) {
        super(r, g, b);
    }

    public DnDColor(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    public DnDColor(ColorSpace cspace, float[] components, float alpha) {
        super(cspace, components, alpha);
    }

    public DnDColor(Color selectedColor) {
        super(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue(), selectedColor.getAlpha());
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone() {
        return new DnDColor(getRed(), getGreen(), getBlue(), getAlpha());
    }
}
