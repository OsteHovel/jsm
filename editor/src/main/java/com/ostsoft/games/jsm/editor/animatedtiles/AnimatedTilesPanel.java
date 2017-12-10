package com.ostsoft.games.jsm.editor.animatedtiles;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.palette.PaletteBox;
import com.ostsoft.games.jsm.palette.Palette;

import javax.swing.*;
import java.awt.*;

public class AnimatedTilesPanel extends AnimationPanel {
    public AnimatedTilesPanel(EditorData editorData) {
        super(editorData);

        PaletteBox paletteBox = new PaletteBox(editorData);
        paletteBox.addActionListener(e -> {
            if (paletteBox.getSelectedItem() != null) {
                animationPanelData.setPalette((Palette) paletteBox.getSelectedItem());
                editorData.fireEvent(EventType.ANIMATION);
            }
        });

        paletteBox.setSelectedIndex(6);

        JComboBox<String> jComboBox = getBox();
        jComboBox.addActionListener(e -> {
            int selectedIndex = jComboBox.getSelectedIndex();
            animationPanelData.setAnimation(editorData.getSuperMetroid().getTileEntries().get(selectedIndex).getTileAnimation());
            java.util.List<AnimationFrame> animationFrames = animationPanelData.getAnimation().getAnimationFrames();
            if (animationFrames.size() > 0) {
                animationPanelData.setAnimationFrame(animationFrames.get(0));
            }
            editorData.fireEvent(EventType.ANIMATION);
        });

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(paletteBox);
        verticalBox.add(jComboBox);
        add(verticalBox, BorderLayout.NORTH);

        jComboBox.setSelectedIndex(1);
    }

    private JComboBox<String> getBox() {
        String poseList[] = new String[]{
                "Blank data",
                "Ground spikes",
                "Wall spikes",
                "Water Surface in Background",
                "Lava / Acid Background, old Tourian set",
                "Lava / Acid Background, Tourian",
                "Metroid in Background Monitor",
                "Treadmill, moving right",
                "Treadmill, moving left",
                "Brinstar Sucker Plant",
                "Falling Sand Ceiling Tiles",
                "Falling Sand"
        };

        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setModel(new DefaultComboBoxModel<>(poseList));
        return jComboBox;
    }

}
