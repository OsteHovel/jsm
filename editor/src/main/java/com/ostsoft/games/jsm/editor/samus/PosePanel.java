package com.ostsoft.games.jsm.editor.samus;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.samus.SamusAnimation;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanel;
import com.ostsoft.games.jsm.editor.common.Progress;
import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.list.ListModel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.editor.palette.PaletteBox;
import com.ostsoft.games.jsm.editor.tilemap.VerticalWrapList;
import com.ostsoft.games.jsm.palette.Palette;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.concurrent.FutureTask;

public class PosePanel extends AnimationPanel {
    private JSplitPane splitPane;

    public PosePanel(EditorData editorData) {
        super(editorData);

        JComboBox paletteBox = new PaletteBox(editorData);
        paletteBox.addActionListener(e -> {
            animationPanelData.setPalette((Palette) paletteBox.getSelectedItem());
            editorData.fireEvent(EventType.ANIMATION);
        });
        add(paletteBox, BorderLayout.NORTH);


        VerticalWrapList<SamusAnimation> poseList = new VerticalWrapList<>();
        poseList.setSelectionForeground(new Color(poseList.getSelectionBackground().getRed(), poseList.getSelectionBackground().getGreen(), poseList.getSelectionBackground().getBlue(), 128));
        poseList.setCellRenderer(new CustomListRenderer<>(panelData, false, true));
        poseList.setModel(new ListModel<>(editorData.getSuperMetroid().getSamusAnimations()));
        poseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        poseList.addListSelectionListener(e -> {
            if (poseList.getSelectedValue() instanceof SamusAnimation) {
                SamusAnimation samusAnimation = (SamusAnimation) poseList.getSelectedValue();
                animationPanelData.setAnimation(samusAnimation);

                java.util.List<AnimationFrame> animationFrames = animationPanelData.getAnimation().getAnimationFrames();
                if (animationFrames.size() > 0) {
                    animationPanelData.setAnimationFrame(animationFrames.get(0));
                }
                editorData.fireEvent(EventType.ANIMATION);
            }
        });

        splitPane.setResizeWeight(0.25d);
        splitPane.add(new JScrollPane(poseList), JSplitPane.LEFT);
        add(splitPane, BorderLayout.CENTER);

        paletteBox.setSelectedIndex(4);
        poseList.setSelectedIndex(0);

        JMenuItem exportAllPoses = new JMenuItem("Export all poses");
        exportAllPoses.addActionListener(e -> {
            new Thread(new Progress(Collections.singletonList(new FutureTask<>(() -> {
                SamusExport samusExport = new SamusExport();
                FileChooserUtil.saveImageAsFile(samusExport.export(editorData.getSuperMetroid(), animationPanelData.getPalette()), "poses.png");
            }, null)))).start();
        });
        mnTools.add(exportAllPoses);
    }

    @Override
    protected void addMainComponent(JComponent component) {
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.splitPane.add(component, JSplitPane.RIGHT);
    }

}
