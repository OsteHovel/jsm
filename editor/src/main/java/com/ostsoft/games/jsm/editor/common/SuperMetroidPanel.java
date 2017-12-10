package com.ostsoft.games.jsm.editor.common;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;

import javax.swing.*;
import java.awt.*;

public class SuperMetroidPanel extends EditorPanel {
    public SuperMetroidPanel(EditorData editorData) {
        super(editorData, new PanelData(editorData));
    }

    public SuperMetroidPanel() {
        super(new EditorData(null, null), null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        Box verticalBox = Box.createVerticalBox();
        add(verticalBox);

        JLabel lblPoses = new JLabel("Poses");
        verticalBox.add(lblPoses);

        JButton btnPoses = new JButton("Unload");
        verticalBox.add(btnPoses);

        Box verticalBox_1 = Box.createVerticalBox();
        add(verticalBox_1);

        JLabel lblCredits = new JLabel("Poses");
        verticalBox_1.add(lblCredits);

        JButton btnCredits = new JButton("Load");
        verticalBox_1.add(btnCredits);

        Box verticalBox_2 = Box.createVerticalBox();
        add(verticalBox_2);

        JLabel label_1 = new JLabel("Poses");
        verticalBox_2.add(label_1);

        JButton button_1 = new JButton("Load");
        verticalBox_2.add(button_1);

        Box verticalBox_3 = Box.createVerticalBox();
        add(verticalBox_3);

        JLabel label_2 = new JLabel("Poses");
        verticalBox_3.add(label_2);

        JButton button_2 = new JButton("Load");
        verticalBox_3.add(button_2);

        Box verticalBox_4 = Box.createVerticalBox();
        add(verticalBox_4);

        JLabel label_3 = new JLabel("Poses");
        verticalBox_4.add(label_3);

        JButton button_3 = new JButton("Load");
        verticalBox_4.add(button_3);
    }


}
