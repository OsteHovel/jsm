package com.ostsoft.games.jsm.util;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private Image image = null;

    public ImagePanel(Image image) {
        super();
        this.image = image;
        setForeground(null);
    }

    public ImagePanel() {
        super();
        setForeground(null);
    }

    @Override
    public void paint(Graphics g) {
        if (isBackgroundSet()) {
            g.setColor(getBackground());
        }
        else {
            g.setColor(TRANSPARENT);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
        if (isForegroundSet()) {
            g.setColor(getForeground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.getWidth(this), image.getHeight(this));
        }
        return super.getPreferredSize();

    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        setSize(image.getWidth(this), image.getHeight(this));
        repaint();
    }
}
