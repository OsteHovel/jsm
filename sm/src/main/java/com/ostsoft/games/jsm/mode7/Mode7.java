package com.ostsoft.games.jsm.mode7;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.graphics.GraphicSet;
import com.ostsoft.games.jsm.util.ImagePanel;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mode7 {

    public Mode7(SuperMetroid superMetroid) {
        for (int i = 17; i < 21; i++) {
            GraphicSet graphicSet = superMetroid.getGraphicSets().get(i);
            int size = 8;

            BufferedImage scaleImage = new BufferedImage(16 * 16 * size, 3 * 16 * 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) scaleImage.getGraphics();
            drawTiles(16, graphicSet, g);
            g.dispose();

            ImagePanel imagePanel = new ImagePanel(scaleImage);

            JFrame jFrame = new JFrame();
            JScrollPane scrollPane = new JScrollPane(imagePanel);
            jFrame.add(scrollPane, BorderLayout.CENTER);
            jFrame.setTitle("jSM - Mode7 " + i);
            jFrame.setSize(1024, 800);
            jFrame.setVisible(true);
            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    protected void drawTiles(int levelWidth, GraphicSet graphicSet, Graphics2D g) {
        int width = levelWidth * 8;
        byte[] tiles = graphicSet.sceneryTiles;
        for (int i = 0; i < tiles.length / 2; i++) {
            int y = i / width;
            int x = i - (y * width);

            int imageX = x * 8;
            int imageY = y * 8;
            g.drawImage(ImageUtil.getImageFromTile(tiles, Byte.toUnsignedInt(tiles[i * 2]), graphicSet.palette.getColors(), 0, 8, true), imageX, imageY, imageX + 8, imageY + 8, 0, 0, 8, 8, null);
        }
    }
}

