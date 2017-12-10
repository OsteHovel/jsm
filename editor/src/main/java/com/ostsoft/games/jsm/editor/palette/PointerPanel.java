package com.ostsoft.games.jsm.editor.palette;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;

import javax.swing.*;
import java.awt.*;

public class PointerPanel extends EditorPanel {

    public PointerPanel(EditorData editorData) {
        super(editorData, new PanelData(editorData));
        setLayout(new BorderLayout(0, 0));

        int size = 32;
        JPanel jPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);


                int y = 0;
                for (Pointer pointer : Pointer.values()) {
                    g.drawString(pointer.name() + " - 0x" + Integer.toHexString(pointer.pointer).toUpperCase(), 0, y * size + size / 2);
                    g.drawLine(0, (y + 1) * size, 1024, (y + 1) * size);


                    y++;
                }
            }
        };
        int maxY = Pointer.values().length * size;
        Dimension d = new Dimension(300, maxY);
        jPanel.setSize(d);
        jPanel.setMinimumSize(d);
        jPanel.setPreferredSize(d);

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        add(jScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {

    }
}

