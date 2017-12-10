package com.ostsoft.games.jsm.editor.level;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.editor.common.Progress;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.graphics.GraphicSet;
import com.ostsoft.games.jsm.graphics.PriorityType;
import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.jsm.room.state.State;
import com.ostsoft.games.jsm.room.state.Tile;
import com.ostsoft.games.jsm.util.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Collections;
import java.util.concurrent.FutureTask;

public class LevelPanel extends EditorPanel {
    private final ImagePanel imagePanel = new ImagePanel();
    private String roomName;

    public LevelPanel(EditorData editorData) {
        super(editorData, new PanelData(editorData));

        java.util.List<Room> rooms = editorData.getSuperMetroid().getRooms();
        String roomList[] = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            roomList[i] = Integer.toHexString(rooms.get(i).getOffset()).toUpperCase();
        }

        JComboBox<String> jComboBox = new JComboBox<>(roomList);

        jComboBox.addActionListener(e -> {
            JComboBox source = (JComboBox) e.getSource();
            new Thread(new Progress(Collections.singletonList(new FutureTask<>(() -> {
                updateRoom(editorData, rooms, imagePanel, source.getSelectedItem());
            }, null)))).start();
        });

        setLayout(new BorderLayout(0, 0));
        add(jComboBox, BorderLayout.NORTH);

        imagePanel.setForeground(null);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        add(scrollPane, BorderLayout.CENTER);

        JMenuItem exportRoom = new JMenuItem("Export");
        exportRoom.addActionListener(e -> {
            if (roomName == null) {
                ErrorUtil.displayErrorBox("Please open a room first");
                return;
            }
            FileChooserUtil.saveImageAsFile((RenderedImage) imagePanel.getImage(), roomName + ".png");
        });
        mnTools.add(exportRoom);

        jComboBox.setSelectedIndex(0);
    }

    private void updateRoom(EditorData editorData, java.util.List<Room> rooms, ImagePanel imagePanel, Object selectedItem) {
        for (Room room : rooms) {
            if (selectedItem.equals(Integer.toHexString(room.getOffset()).toUpperCase())) {
                roomName = Integer.toHexString(room.getOffset()).toUpperCase();
                updateLevel(editorData, imagePanel, room);
                break;
            }
        }
    }

    private void updateLevel(EditorData editorData, ImagePanel imagePanel, Room room) {
        int levelWidth = room.getHeader().width * 16;
        int levelHeight = room.getHeader().height * 16;
        State state = room.getStates().get(0);
        GraphicSet graphicSet = editorData.getSuperMetroid().getGraphicSets().get(state.graphicSet);

        BufferedImage image = new BufferedImage(levelWidth * 16, levelHeight * 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        if (state.background != null && state.background.background != null && !state.isLayer2Background()) {
            BufferedImage backgroundImage = editorData.getSuperMetroid().getGraphicSets().get(state.graphicSet).generateImageFromTileTable8x8(state.background.background, 32, false);
            for (int y = 0; y < backgroundImage.getHeight() / levelHeight * 16; y++) {
                for (int x = 0; x < backgroundImage.getWidth() / levelWidth * 16; x++) {
                    g.drawImage(backgroundImage, x * backgroundImage.getWidth(), y * backgroundImage.getHeight(), null);
                }
            }
        }

        if (state.layer1And2.layer2Tiles != null && state.isLayer2Background()) {
            drawTiles(levelWidth, graphicSet, g, state.layer1And2.layer2Tiles);
        }
        drawTiles(levelWidth, graphicSet, g, state.layer1And2.tiles);

        g.dispose();
        imagePanel.setImage(image);
    }

    protected void drawTiles(int levelWidth, GraphicSet graphicSet, Graphics2D g, java.util.List<Tile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            int y = i / levelWidth;
            int x = i - (y * levelWidth);

            Tile tile = tiles.get(i);
            int imageX = x * 16;
            int imageY = y * 16;
            if (tile.isHFlip() && tile.isVFlip()) {
                g.drawImage(graphicSet.generateImageFromTile(tile.blockID, PriorityType.BOTH, 4), imageX, imageY, imageX + 16, imageY + 16, 16, 16, 0, 0, null);
            }
            else if (tile.isHFlip()) {
                g.drawImage(graphicSet.generateImageFromTile(tile.blockID, PriorityType.BOTH, 4), imageX, imageY, imageX + 16, imageY + 16, 16, 0, 0, 16, null);
            }
            else if (tile.isVFlip()) {
                g.drawImage(graphicSet.generateImageFromTile(tile.blockID, PriorityType.BOTH, 4), imageX, imageY, imageX + 16, imageY + 16, 0, 16, 16, 0, null);
            }
            else {
                g.drawImage(graphicSet.generateImageFromTile(tile.blockID, PriorityType.BOTH, 4), imageX, imageY, imageX + 16, imageY + 16, 0, 0, 16, 16, null);
            }
        }
    }
}