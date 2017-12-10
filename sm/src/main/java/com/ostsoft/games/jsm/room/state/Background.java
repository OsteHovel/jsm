package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.graphics.GraphicSet;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
    public int offset;
    public int backgroundPointer;
    public byte[] background;

    public Background(ByteStream stream, int offset) {
        this.offset = offset;

        int header = stream.readReversedUnsignedShort();
        if (header != 0x4) {
            return;
        }

        backgroundPointer = stream.readReversedUnsigned3Bytes();
        background = CompressUtil.decompress(stream, BitHelper.snesToPc(backgroundPointer));
    }

    public void printDebug(GraphicSet graphicSet) {
        JFrame jframe = new JFrame() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                BufferedImage img = graphicSet.generateImageFromTileTable8x8(background, 32, false);
                g.drawImage(img, 60, 60, img.getWidth() * 2, img.getHeight() * 2, null);
            }
        };
        jframe.setVisible(true);
        jframe.setSize(1024, 1200);
    }


}
