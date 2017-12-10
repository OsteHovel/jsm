package com.ostsoft.games.jsm.credits;


import com.ostsoft.games.jsm.common.DnD;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlankLine extends CreditLine implements DnD {
    public int numberOfLines;

    public BlankLine(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    @Override
    public Image getImage() {
        BufferedImage image = new BufferedImage(32 * 8, numberOfLines * 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        return image;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public BlankLine clone() {
        return new BlankLine(numberOfLines);
    }
}
