package com.ostsoft.games.jsm.editor.common.panel;

import javax.swing.*;
import java.awt.*;

class GhostGlassPane extends JComponent {
    private DnDTabbedPane tabbedPane;

    public GhostGlassPane(DnDTabbedPane tabbedPane) {
        super();
        this.tabbedPane = tabbedPane;
        setOpaque(false);
    }

    public void setTargetTabbedPane(DnDTabbedPane tab) {
        tabbedPane = tab;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        //tabbedPane.paintDropLine(g2);
        Rectangle rect = tabbedPane.getDropLineRect();
        if (rect != null) {
            Rectangle r = SwingUtilities.convertRectangle(tabbedPane, rect, this);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            g2.setColor(Color.RED);
            g2.fill(r);
            //tabbedPane.paintDropLine(g2);
        }
        g2.dispose();
    }
}
