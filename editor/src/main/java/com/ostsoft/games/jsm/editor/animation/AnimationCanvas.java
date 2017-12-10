package com.ostsoft.games.jsm.editor.animation;

import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.observer.command.MoveSpriteCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AnimationCanvas extends JPanel implements Observer {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;
    private int offsetX = 96;
    private int offsetY = 96;

    public AnimationCanvas(EditorData editorData, AnimationPanelData animationPanelData) {
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
        setDoubleBuffered(true);

        setPreferredSize(new Dimension(640, 480));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private SpriteMapEntry dragEntry = null;
            private int startDragX = 0;
            private int startDragY = 0;
            private int clickOffsetX = 0;
            private int clickOffsetY = 0;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragEntry == null) {
                    return;
                }
                dragEntry.setX((int) ((e.getX() - offsetX) / animationPanelData.getScale()) + clickOffsetX);
                dragEntry.setY((int) ((e.getY() - offsetY) / animationPanelData.getScale()) + clickOffsetY);
                refresh();

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (animationPanelData.getAnimationFrame() == null) {
                    return;
                }
                int x = (int) ((e.getX() - offsetX) / animationPanelData.getScale());
                int y = (int) ((e.getY() - offsetY) / animationPanelData.getScale());

                for (SpriteMapEntry spriteMapEntry : animationPanelData.getAnimationFrame().getSpriteMapEntries()) {
                    int size = (spriteMapEntry.is16x16tile() ? 16 : 8);
                    if (x > spriteMapEntry.getX() && x < spriteMapEntry.getX() + size && y > spriteMapEntry.getY() && y < spriteMapEntry.getY() + size) {
                        clickOffsetX = spriteMapEntry.getX() - x;
                        clickOffsetY = spriteMapEntry.getY() - y;
                        startDragX = spriteMapEntry.getX();
                        startDragY = spriteMapEntry.getY();
                        dragEntry = spriteMapEntry;
                        if (animationPanelData.getSelectEntry() != spriteMapEntry) {
                            animationPanelData.setSelectEntry(spriteMapEntry);
                            editorData.fireEvent(EventType.SPRITE_SELECTED);
                        }
                        return;
                    }
                }
                animationPanelData.setSelectEntry(null);
                editorData.fireEvent(EventType.SPRITE_SELECTED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragEntry == null) {
                    return;
                }

                dragEntry.setX(startDragX);
                dragEntry.setY(startDragY);
                editorData.getCommandCenter().executeCommand(new MoveSpriteCommand(editorData, dragEntry
                        , (int) ((e.getX() - offsetX) / animationPanelData.getScale()) + clickOffsetX
                        , (int) ((e.getY() - offsetY) / animationPanelData.getScale()) + clickOffsetY));
                dragEntry = null;
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                SpriteMapEntry selectEntry = animationPanelData.getSelectEntry();
                if (selectEntry != null) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        editorData.getCommandCenter().executeCommand(new MoveSpriteCommand(editorData, selectEntry, selectEntry.getX(), selectEntry.getY() - 1));
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        editorData.getCommandCenter().executeCommand(new MoveSpriteCommand(editorData, selectEntry, selectEntry.getX(), selectEntry.getY() + 1));
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        editorData.getCommandCenter().executeCommand(new MoveSpriteCommand(editorData, selectEntry, selectEntry.getX() - 1, selectEntry.getY()));
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        editorData.getCommandCenter().executeCommand(new MoveSpriteCommand(editorData, selectEntry, selectEntry.getX() + 1, selectEntry.getY()));
                    }
                }
            }
        });

        editorData.addObserver(this);
    }


    public void refresh() {
        if (animationPanelData.getAnimationFrame() == null) {
            return;
        }
        repaint();
    }


    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g = (Graphics2D) graphics;
        if (animationPanelData.getAnimationFrame() == null || animationPanelData.getPalette() == null) {
            return;
        }

        offsetX = getWidth() / 2;
        offsetY = getHeight() / 2;

        BufferedImage image = animationPanelData.getAnimationFrame().getImage(animationPanelData.getPalette().getColors());
        g.drawImage(image
                , (int) (0 - ((float) image.getWidth() / 2f) * animationPanelData.getScale()) + offsetX
                , (int) (0 - ((float) image.getHeight() / 2f) * animationPanelData.getScale()) + offsetY
                , (int) (image.getWidth() * animationPanelData.getScale())
                , (int) (image.getHeight() * animationPanelData.getScale()), null);

        g.setColor(Color.CYAN);
        g.drawLine(offsetX, 0, offsetX, getHeight());
        g.drawLine(0, offsetY, getWidth(), offsetY);


    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        switch (eventType) {
            case ANIMATION:
            case ANIMATION_UPDATED:
            case ANIMATION_FRAME:
            case ANIMATION_FRAME_UPDATED:
            case SPRITE_SELECTED:
            case SPRITE_MOVED:
            case SPRITE_UPDATED:
            case PALETTE_UPDATED:
            case TILE_UPDATED:
                refresh();
        }
    }
}
