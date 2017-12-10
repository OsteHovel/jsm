package com.ostsoft.games.jsm.mode7;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.palette.PaletteEnum;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;
import com.ostsoft.games.jsm.util.ImagePanel;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Mode7Test {
    private final ImagePanel imagePanel;
    private float scale = 1f;
    private byte[] tiles;
    private Palette palette;
    private BufferedImage image;

    public Mode7Test(SuperMetroid superMetroid) {
        palette = superMetroid.getPaletteManager().getPalette(PaletteEnum.TITLE_METROID);
        tiles = CompressUtil.decompress(superMetroid.getStream(), 0xAA5E1);

        image = new BufferedImage(16 * 8, 16 * 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        drawTiles(16, g);
        g.dispose();

        imagePanel = new ImagePanel(image);

        JFrame jFrame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        jFrame.add(scrollPane, BorderLayout.CENTER);
        jFrame.setTitle("jSM - Mode7");
        jFrame.setSize(1024, 800);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        MouseWheelListener zoomWheelListener = e -> {
            if (e.isControlDown()) {
                scale -= (e.getPreciseWheelRotation() * 0.1f);
                update();
                e.consume();
            }
        };
        imagePanel.addMouseWheelListener(zoomWheelListener);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        ByteStream byteStream = null;
        try {
            FileInputStream stream = new FileInputStream("sm.smc");
            int len = stream.available();
            byte[] bytes = new byte[len];
            stream.read(bytes, 0, len);
            byteStream = new ByteStream(bytes);
        } catch (IOException e) {

        }
        new Mode7Test(new SuperMetroid(byteStream, SuperMetroid.Flag.PALETTES.value | SuperMetroid.Flag.GRAPHICSETS.value));

    }

    private void update() {
        imagePanel.setImage(image.getScaledInstance((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), Image.SCALE_DEFAULT));
    }

    protected void drawTiles(int levelWidth, Graphics2D g) {
        int width = levelWidth * 8;

        for (int i = 0; i < tiles.length / 2; i++) {
            int y = i / width;
            int x = i - (y * width);

            int imageX = x * 8;
            int imageY = y * 8;
            g.drawImage(ImageUtil.getImageFromTile(tiles, i, palette.getColors(), 0, 8, false), imageX, imageY, imageX + 8, imageY + 8, 0, 0, 8, 8, null);
        }
    }
}
