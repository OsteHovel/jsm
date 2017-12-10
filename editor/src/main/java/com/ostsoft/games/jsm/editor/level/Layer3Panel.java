package com.ostsoft.games.jsm.editor.level;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.layer3.Layer3;
import com.ostsoft.games.jsm.util.ImagePanel;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Layer3Panel extends EditorPanel {

    public Layer3Panel(EditorData editorData) {
        super(editorData, new PanelData(editorData));
        editorData.getSuperMetroid().load(SuperMetroid.Flag.LAYER3.value);

        Layer3[] layer3List = editorData.getSuperMetroid().getLayer3List();

        java.util.List<Layer3> compactedLayer3List = new ArrayList<>();
        for (Layer3 layer3 : layer3List) {
            if (layer3 != null) {
                compactedLayer3List.add(layer3);
            }
        }
        ImagePanel imagePanel = new ImagePanel();
        JComboBox<Object> jComboBox = new JComboBox<>(compactedLayer3List.toArray(new Layer3[0]));
        jComboBox.addActionListener(e -> {
            JComboBox source = (JComboBox) e.getSource();
            Layer3 layer3 = (Layer3) source.getSelectedItem();
            if (layer3 == null) {
                return;
            }

            BufferedImage image = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) image.getGraphics();
//                g.setBackground(Color.BLACK);
//                g.clearRect(0, 0, image.getWidth(), image.getHeight());

            for (int i = 0; i < layer3.palette.getColors().size(); i++) {
                Color color = layer3.palette.getColors().get(i);
                g.setColor(color);
                g.fillRect(10 + (i * 5), 0, 10, 20);
            }
            BufferedImage image2 = ImageUtil.getImageFromTiles(layer3.tiles, layer3.palette.getColors(), 0, 16, 2);
            BufferedImage image3 = ImageUtil.generateImageFromTileTable8x8(layer3.tileTable, layer3.tiles, 32, false, layer3.palette, 2);

            g.drawImage(image2, 10, 30, image2.getWidth() * 2, image2.getHeight() * 2, null);
            g.drawImage(image3, image2.getWidth() + 128, 30, image3.getWidth() * 2, image3.getHeight() * 2, null);

            g.dispose();
            imagePanel.setImage(image);
        });

        setLayout(new BorderLayout(0, 0));
        add(jComboBox, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        add(scrollPane, BorderLayout.CENTER);

        jComboBox.setSelectedIndex(0);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {

    }
}

