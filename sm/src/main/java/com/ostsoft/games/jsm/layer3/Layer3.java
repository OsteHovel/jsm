package com.ostsoft.games.jsm.layer3;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Layer3 {

    public Layer3Type layer3Type;
    public byte[] tiles;
    public Layer3Tile[] layer3Tiles;
    public byte[] tileTable;

    /* Test */
    public Palette palette;

    public Layer3(ByteStream stream, Layer3Type layer3Type) {
        this.layer3Type = layer3Type;
        tileTable = new byte[2110];
        stream.read(tileTable, 0, tileTable.length);

        int offset = 0x89AA00 + 0x48;

        palette = new Palette("Layer 3: " + layer3Type.name(), offset, false, 128);
        palette.load(stream);


        tiles = new byte[4096];
        stream.setPosition(Pointer.Layer3Tiles.pointer);
        stream.read(tiles, 0, tiles.length);

    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(layer3Type.value).toUpperCase() + " - " + layer3Type.name();
    }

    public void printDebug() {
        java.util.List<DnDColor> colors = palette.getColors();


        BufferedImage image = ImageUtil.getImageFromTiles(tiles, colors, 0, 16, 2);
        BufferedImage image2 = ImageUtil.generateImageFromTileTable8x8(tileTable, tiles, 32, false, palette, 2);


        JFrame jframe = new JFrame() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
//                g.setColor(Color.BLACK);
//                g.fillRect(0, 0, 1024, 1024);

                for (int i = 0; i < colors.size(); i++) {
                    Color color = colors.get(i);
                    g.setColor(color);
                    g.fillRect(10 + (i * 5), 30, 10, 20);
                }
                g.drawImage(image, 10, 60, image.getWidth() * 2, image.getHeight() * 2, null);
                g.drawImage(image2, 10, 256, image.getWidth() * 2, image.getHeight() * 2, null);
            }
        };
        jframe.setVisible(true);
        jframe.setSize(1024, 1200);


    }
}
