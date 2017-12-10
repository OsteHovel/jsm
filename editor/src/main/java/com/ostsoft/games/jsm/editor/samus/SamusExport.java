package com.ostsoft.games.jsm.editor.samus;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.animation.samus.SamusAnimation;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SamusExport {

    public BufferedImage export(SuperMetroid superMetroid, Palette palette) {
        BufferedImage image = new BufferedImage(67 * 64, (0xFC * 64), BufferedImage.TYPE_INT_ARGB);
        List<DnDColor> colors = palette.getColors();

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        for (int pose = 0; pose < 0xFC; pose++) {
            SamusAnimation samusAnimation = superMetroid.getSamusAnimations().get(pose);
            for (int j = 0; j < samusAnimation.animationFrames.size(); j++) {
                BufferedImage poseImage = samusAnimation.animationFrames.get(j).getImage(colors);

                g.drawImage(
                        poseImage,
                        (j * 64) + poseImage.getWidth() / 2,
                        (pose * 64) + (poseImage.getHeight() / 2),
                        null
                );
            }
        }
        g.dispose();

        return image;
    }
}
