package com.ostsoft.games.jsm.editor.enemy;

import com.ostsoft.games.jsm.RoomList;
import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.enemy.Enemy;
import com.ostsoft.games.jsm.palette.Palette;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class EnemyPanel extends AnimationPanel {
    public EnemyPanel(EditorData editorData) {
        super(editorData);
        editorData.getSuperMetroid().load(SuperMetroid.Flag.ROOMS.value | SuperMetroid.Flag.ENEMIES.value, RoomList.getRoomList(), Collections.emptyList());

        ArrayList<Enemy> enemies = new ArrayList<>(editorData.getSuperMetroid().getEnemies().values());
        Collections.sort(enemies);
        Enemy[] enemiesArray = enemies.toArray(new Enemy[enemies.size()]);

        JComboBox<Enemy> jComboBox = new JComboBox<>(enemiesArray);

        jComboBox.addActionListener(e -> {
            Enemy enemy = (Enemy) jComboBox.getSelectedItem();
            if (enemy == null) {
                return;
            }

            animationPanelData.setAnimation(enemy.getEnemyAnimation());
            java.util.List<AnimationFrame> animationFrames = animationPanelData.getAnimation().getAnimationFrames();
            if (animationFrames.size() > 0) {
                animationPanelData.setAnimationFrame(animationFrames.get(0));
            }

            Palette palette = new Palette("Enemy palette", enemy.palettePointer, false, 32);
            palette.load(editorData.getSuperMetroid().getStream());
            animationPanelData.setPalette(palette);

            editorData.fireEvent(EventType.ANIMATION);
        });

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(jComboBox);
        add(verticalBox, BorderLayout.NORTH);


        jComboBox.setSelectedIndex(0);
    }
}
