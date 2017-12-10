package com.ostsoft.games.jsm.editor.samus;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanel;
import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.tilemap.VerticalWrapList;
import com.ostsoft.games.jsm.projectile.Projectile;

import javax.swing.*;
import java.awt.*;


public class ProjectilePanel extends AnimationPanel {

    private JSplitPane splitPane;

    public ProjectilePanel(EditorData editorData) {
        super(editorData);
        editorData.getSuperMetroid().load(SuperMetroid.Flag.PROJECTILES.value);


//        PaletteBox paletteBox = new PaletteBox(editorData);
//        paletteBox.addActionListener(e -> {
//            if (paletteBox.getSelectedItem() != null) {
//                animationPanelData.setPalette((Palette) paletteBox.getSelectedItem());
//                editorData.fireEvent(EventType.ANIMATION);
//            }
//        });
//
//        paletteBox.setSelectedIndex(10);


        VerticalWrapList<Projectile> projectileList = new VerticalWrapList<>();
        projectileList.setSelectionForeground(new Color(projectileList.getSelectionBackground().getRed(), projectileList.getSelectionBackground().getGreen(), projectileList.getSelectionBackground().getBlue(), 128));
        projectileList.setCellRenderer(new CustomListRenderer<>(panelData, false, true));
        projectileList.setModel(new com.ostsoft.games.jsm.editor.common.list.ListModel<>(editorData.getSuperMetroid().getProjectiles()));
        projectileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        projectileList.addListSelectionListener(e -> {
            if (projectileList.getSelectedValue() != null) {
                Projectile projectile = (Projectile) projectileList.getSelectedValue();
                animationPanelData.setAnimation(projectile.projectileAnimations[0]);

                java.util.List<AnimationFrame> animationFrames = animationPanelData.getAnimation().getAnimationFrames();
                if (animationFrames.size() > 0) {
                    animationPanelData.setAnimationFrame(animationFrames.get(0));
                }
                animationPanelData.setPalette(projectile.palette);
                editorData.fireEvent(EventType.ANIMATION);
            }
        });

        splitPane.setResizeWeight(0.25d);
        splitPane.add(new JScrollPane(projectileList), JSplitPane.LEFT);
        add(splitPane, BorderLayout.CENTER);


    }

    @Override
    protected void addMainComponent(JComponent component) {
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.splitPane.add(component, JSplitPane.RIGHT);
    }

}
