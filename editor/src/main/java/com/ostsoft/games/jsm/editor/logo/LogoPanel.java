package com.ostsoft.games.jsm.editor.logo;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.Logo;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.palette.PaletteBox;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.palette.PaletteEnum;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LogoPanel extends AnimationPanel {
    public LogoPanel(EditorData editorData) {
        super(editorData);
        animationCanvas.setBackground(Color.BLACK);

        PaletteBox paletteBox = new PaletteBox(editorData);
        paletteBox.addActionListener(e -> {
            animationPanelData.setPalette((Palette) paletteBox.getSelectedItem());
            editorData.fireEvent(EventType.ANIMATION);
        });

        List<Logo> logos = editorData.getSuperMetroid().getLogos();
        JComboBox<Logo> logoBox = new JComboBox<>(logos.toArray(new Logo[logos.size()]));

        logoBox.addActionListener(e -> {
            int selectedIndex = logoBox.getSelectedIndex();
            animationPanelData.setAnimation(editorData.getSuperMetroid().getLogos().get(selectedIndex));
            List<AnimationFrame> animationFrames = animationPanelData.getAnimation().getAnimationFrames();
            if (animationFrames.size() > 0) {
                animationPanelData.setAnimationFrame(animationFrames.get(0));
            }
            editorData.fireEvent(EventType.ANIMATION);
        });

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(paletteBox);
        verticalBox.add(logoBox);
        add(verticalBox, BorderLayout.NORTH);

        animationPanelData.setPalette(editorData.getSuperMetroid().getPaletteManager().getPalette(PaletteEnum.NINTENDO_LOGO));
        if (animationPanelData.getPalette() != null) {
            for (int i = 0; i < paletteBox.getModel().getSize(); i++) {
                if (paletteBox.getModel().getElementAt(i) == animationPanelData.getPalette()) {
                    paletteBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        else {
            paletteBox.setSelectedIndex(0);
        }

        logoBox.setSelectedIndex(2);
    }
}
