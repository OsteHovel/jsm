package com.ostsoft.games.jsm.editor.samus;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.projectile.DamageAndDirection;
import com.ostsoft.games.jsm.projectile.Projectile;
import com.ostsoft.games.jsm.util.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.List;

public class ProjectileViewerPanel extends EditorPanel {
    private final Observer observer;
    private final ImagePanel imagePanel;

    public ProjectileViewerPanel(EditorData editorData) {
        super(editorData, new PanelData(editorData));
        editorData.getSuperMetroid().load(SuperMetroid.Flag.PROJECTILES.value);

        List<Projectile> projectiles = editorData.getSuperMetroid().getProjectiles();

        String projectileList[] = new String[projectiles.size()];
        for (int i = 0; i < projectiles.size(); i++) {
            projectileList[i] = projectiles.get(i).projectileType.name();
        }
        imagePanel = new ImagePanel();

        JComboBox<String> jComboBox = new JComboBox<>(projectileList);
        jComboBox.addActionListener(e -> {
            JComboBox source = (JComboBox) e.getSource();
            for (Projectile projectile : projectiles) {
                if (projectile.projectileType.name().equals(source.getSelectedItem())) {
                    BufferedImage image = getBufferedImage(projectile);
                    imagePanel.setImage(image);
                    break;
                }
            }
        });


        observer = (eventType, message) ->
        {
//            if (eventType == EventType.SCALE || eventType == EventType.RELOAD) {
//                for (Projectile projectile : projectiles) {
//                    if (projectile.projectileType.name().equals(jComboBox.getSelectedItem())) {
//                        BufferedImage image = getBufferedImage(projectile);
//                        imagePanel.setImage(image);
//                        break;
//                    }
//                }
//            }
        };
        editorData.addObserver(observer);

        setLayout(new BorderLayout(0, 0));
        add(jComboBox, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        add(scrollPane, BorderLayout.CENTER);

        jComboBox.setSelectedIndex(0);

        JMenuItem exportProjectiles = new JMenuItem("Export projectiles");
        exportProjectiles.addActionListener(e -> FileChooserUtil.saveImageAsFile((RenderedImage) imagePanel.getImage(), "projectiles.png"));
        mnTools.add(exportProjectiles);
    }

    private BufferedImage getBufferedImage(Projectile projectile) {
        BufferedImage image = new BufferedImage(64 * 25, 74 * 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, image.getWidth(), image.getHeight());

        for (int i = 0; i < 10; i++) {
            Animation animation = projectile.projectileAnimations[i];
            g.setColor(Color.WHITE);
            g.drawString(DamageAndDirection.Direction.getDirection(i).name(), 0, (74 * i) + 10);

            List<DnDColor> colors = projectile.palette.getColors();
            for (int j = 0; j < animation.getAnimationFrames().size(); j++) {
                AnimationFrame animationFrame = animation.getAnimationFrames().get(j);
                g.drawImage(animationFrame.getImage(colors), j * 64, i * 74, null);
            }
        }

        g.dispose();
        return image;
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        super.handlePanelEvent(panelEvent);
        switch (panelEvent) {
            case CLOSEPANEL:
                editorData.removeObserver(observer);
        }

    }
}
