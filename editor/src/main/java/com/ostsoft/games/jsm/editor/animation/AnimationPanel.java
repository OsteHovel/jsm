package com.ostsoft.games.jsm.editor.animation;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.samus.SamusFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.editor.common.util.GifSequenceWriter;
import com.ostsoft.games.jsm.editor.tilemap.TileUtil;
import com.ostsoft.games.jsm.palette.DnDColor;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AnimationPanel extends EditorPanel {
    protected final AnimationPanelData animationPanelData;
    protected final AnimationCanvas animationCanvas;
    private final Observer animationPanelObserver;
    private final JSlider zoomSlider;
    private final AnimationTable table;
    private final AnimationTree tree;

    public AnimationPanel(EditorData editorData) {
        super(editorData, new AnimationPanelData(editorData));
        this.animationPanelData = (AnimationPanelData) panelData;

        setLayout(new BorderLayout(0, 0));

        tree = new AnimationTree(editorData, animationPanelData);
        editorData.addObserver(tree);

        table = new AnimationTable(editorData, animationPanelData);
        editorData.addObserver(table);

        JScrollPane treeScrollPane = new JScrollPane(tree);
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.add(treeScrollPane, BorderLayout.CENTER);
        JScrollPane tableScrollPane = new JScrollPane(table);
        JSplitPane splitPaneTreeTable = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePanel, tableScrollPane);
        splitPaneTreeTable.setResizeWeight(0.5d);

        animationCanvas = new AnimationCanvas(editorData, animationPanelData);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, animationCanvas, splitPaneTreeTable);
        splitPane.setResizeWeight(0.66d);
        addMainComponent(splitPane);

        zoomSlider = new JSlider();
        zoomSlider.setSnapToTicks(false);
        zoomSlider.setPaintLabels(false);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setMinorTickSpacing(25);
        zoomSlider.setMajorTickSpacing(100);
        zoomSlider.setMaximum(5000);
        zoomSlider.setMinimum(10);
        zoomSlider.setValue((int) (animationPanelData.getScale() * 100));
        add(zoomSlider, BorderLayout.SOUTH);

        zoomSlider.addChangeListener(e -> {
            if (!zoomSlider.getValueIsAdjusting()) {
                animationPanelData.setScale(((float) zoomSlider.getValue()) / 100f);
                animationCanvas.refresh();
            }
        });

        this.animationPanelObserver = (eventType, message) -> {
            if (eventType == EventType.ANIMATION_FRAME_UPDATED) {
                AnimationFrame animationFrame = animationPanelData.getAnimationFrame();
                if (animationFrame != null) {
                    if (animationFrame instanceof SamusFrame) {
                        ((SamusFrame) animationFrame).loadTiles(editorData.getSuperMetroid().getDmaTables());
                    }
                }
            }

        };
        editorData.addObserver(this.animationPanelObserver);


        JMenuItem importTiles = new JMenuItem("Import tiles");
        importTiles.addActionListener(e -> {
            if (animationPanelData.getAnimationFrame() == null || animationPanelData.getPalette() == null) {
                return;
            }
            TileUtil.importTiles(editorData, animationPanelData.getPalette().getColors(), animationPanelData.getAnimationFrame().getTiles());
        });
        mnTools.add(importTiles);

        JMenuItem exportTiles = new JMenuItem("Export tiles");
        exportTiles.addActionListener(e -> {
            if (animationPanelData.getAnimationFrame() == null || animationPanelData.getPalette() == null) {
                return;
            }
            TileUtil.exportTiles(animationPanelData.getAnimationFrame().getTiles(), animationPanelData.getPalette());
        });
        mnTools.add(exportTiles);

        JMenuItem exportCurrentFrame = new JMenuItem("Export frame");
        exportCurrentFrame.addActionListener(e -> FileChooserUtil.saveImageAsFile((RenderedImage) getImage(), "animation.png"));
        mnTools.add(exportCurrentFrame);

        JMenuItem exportAnimation = new JMenuItem("Export animation");
        exportAnimation.addActionListener(e -> {
            if (animationPanelData.getAnimation() == null || animationPanelData.getPalette() == null) {
                return;
            }

            File file = FileChooserUtil.saveDialogSingle("animation", true, "Animated GIF (.gif)", "gif");
            if (file == null) {
                return;
            }
            try {
                ImageOutputStream output = new FileImageOutputStream(file);

                List<DnDColor> palette = animationPanelData.getPalette().getColors();
                java.util.List<AnimationFrame> animationFrames = animationPanelData.getAnimation().getAnimationFrames();
                if (animationFrames.size() <= 0) {
                    return;
                }

                // create a gif sequence with the type of the first image, 1 second
                // between frames, which loops continuously
                BufferedImage firstImage = animationFrames.get(0).getImage(palette);
                GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), 10, true);

                writer.writeToSequence(firstImage, animationFrames.get(0).getFrameLength() * 16, true);
                // write out the first image to our sequence...
                for (int i = 1; i < animationFrames.size(); i++) {
                    writer.writeToSequence(animationFrames.get(i).getImage(palette), animationFrames.get(i).getFrameLength() * 16, true);
                }

                writer.close();
                output.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mnTools.add(exportAnimation);

    }

    protected void addMainComponent(JComponent component) {
        add(component, BorderLayout.CENTER);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        super.handlePanelEvent(panelEvent);

        switch (panelEvent) {
            case ZOOM_IN:
            case ZOOM_OUT:
                break;

            case SCALE:
                zoomSlider.setValue((int) (animationPanelData.getScale() * 100));
                break;

            case CLOSEPANEL:
                editorData.removeObserver(table);
                editorData.removeObserver(tree);
                editorData.removeObserver(animationPanelObserver);
                editorData.removeObserver(animationCanvas);
                break;

            default:
                editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "Unsupported action " + panelEvent.name());
        }
    }

    public Image getImage() {
        if (animationPanelData.getAnimationFrame() != null && animationPanelData.getPalette() != null) {
            return animationPanelData.getAnimationFrame().getImage(animationPanelData.getPalette().getColors());
        }
        else {
            return null;
        }
    }
}
