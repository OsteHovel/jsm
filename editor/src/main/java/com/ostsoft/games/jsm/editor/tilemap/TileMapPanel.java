package com.ostsoft.games.jsm.editor.tilemap;

import com.ostsoft.games.jsm.common.HTileMap;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.common.TileMap;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.list.EventListItemTransferHandler;
import com.ostsoft.games.jsm.editor.common.list.EventListModel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.editor.palette.PaletteBox;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class TileMapPanel extends EditorPanel {
    private final TileMapPanelData tileMapPanelData;
    private final Observer observer;
    private final VerticalWrapList<TileMap> tilemapsList;
    private final VerticalWrapList<Tile> currentTileMapList;
    private final VerticalWrapList<Tile> tileList;
    private final TileMapTable table;
    private final TileTransferHandler newHandler;


    public TileMapPanel(EditorData editorData, TileMapPanelData tileMapPanelData) {
        super(editorData, tileMapPanelData);
        this.tileMapPanelData = tileMapPanelData;

        setLayout(new BorderLayout(0, 0));

        tilemapsList = new VerticalWrapList<>();
        tilemapsList.setBackground(Color.BLACK);
        tilemapsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tilemapsList.setCellRenderer(new CustomListRenderer<>(tileMapPanelData, true, false));
        tilemapsList.setSelectionForeground(new Color(tilemapsList.getSelectionBackground().getRed(), tilemapsList.getSelectionBackground().getGreen(), tilemapsList.getSelectionBackground().getBlue(), 128));
        com.ostsoft.games.jsm.editor.common.list.ListModel<TileMap> tilemapsModel = new com.ostsoft.games.jsm.editor.common.list.ListModel<>(tileMapPanelData.getTileMaps());
        tilemapsList.setModel(tilemapsModel);
        tilemapsList.addListSelectionListener(e -> {
            TileMap selectedValue = tilemapsList.getSelectedValue();
            if (tileMapPanelData.getTileMap() != selectedValue) {
                tileMapPanelData.setTileMap(selectedValue);
                editorData.fireEvent(EventType.TILEMAP_SELECTED);
            }
        });
//        tilemapsList.setSize(new Dimension(200, 200));


        currentTileMapList = new VerticalWrapList<>();
        currentTileMapList.setBackground(Color.BLACK);
        currentTileMapList.setCellRenderer(new CustomListRenderer<>(tileMapPanelData, true, false));
        currentTileMapList.setSelectionForeground(new Color(currentTileMapList.getSelectionBackground().getRed(), currentTileMapList.getSelectionBackground().getGreen(), currentTileMapList.getSelectionBackground().getBlue(), 128));
        currentTileMapList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        newHandler = new TileTransferHandler(TransferHandler.COPY_OR_MOVE, tileMapPanelData.getTileType(), tileMapPanelData);
        currentTileMapList.setTransferHandler(newHandler);
        currentTileMapList.setDropMode(DropMode.INSERT);
        currentTileMapList.setDragEnabled(true);
        currentTileMapList.addListSelectionListener(e -> {
            Tile selectedValue = currentTileMapList.getSelectedValue();
            if (tileMapPanelData.getTile() != selectedValue) {
                tileMapPanelData.setTile(selectedValue);
                editorData.fireEvent(EventType.TILEMAP_SELECTED_TILE);
            }
        });

        currentTileMapList.setLayoutOrientation(JList.HORIZONTAL_WRAP);


        tileList = new VerticalWrapList<>();
        tileList.setBackground(Color.BLACK);
        tileList.setCellRenderer(new CustomListRenderer<>(tileMapPanelData, true, false));
        tileList.setSelectionForeground(new Color(tileList.getSelectionBackground().getRed(), tileList.getSelectionBackground().getGreen(), tileList.getSelectionBackground().getBlue(), 128));
        com.ostsoft.games.jsm.editor.common.list.ListModel<Tile> tileModel = new com.ostsoft.games.jsm.editor.common.list.ListModel<>(tileMapPanelData.getTiles());
        tileList.setModel(tileModel);
        tileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tileList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tileList.setTransferHandler(new EventListItemTransferHandler<>(TransferHandler.COPY));
        tileList.setDropMode(DropMode.INSERT);
        tileList.setDragEnabled(true);


        PaletteBox paletteBox = new PaletteBox(editorData);
        paletteBox.addActionListener(e -> {
            if (paletteBox.getSelectedItem() != null) {
                tileMapPanelData.setPalette((Palette) paletteBox.getSelectedItem());
//                for (Tile tile : tileMapPanelData.getTilesBytes()) {
//                    if (tile instanceof SimpleTile) {
//                        ((SimpleTile) tile).setPalette(tileMapPanelData.getPalette());
//                    }
//                }

                refreshList(tilemapsList);
                refreshList(currentTileMapList);
                refreshList(tileList);
            }
        });
        add(paletteBox, BorderLayout.NORTH);

        JScrollPane currentTileMapListScroll = new JScrollPane(currentTileMapList);
        currentTileMapListScroll.addMouseWheelListener(zoomWheelListener);

        JScrollPane tileListScroll = new JScrollPane(tileList);
        tileListScroll.addMouseWheelListener(zoomWheelListener);


        JSplitPane splitPaneVerticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, currentTileMapListScroll, tileListScroll);
        splitPaneVerticalSplit.setResizeWeight(0.5d);
        table = new TileMapTable(editorData, tileMapPanelData);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.addMouseWheelListener(zoomWheelListener);

        JSplitPane splitPaneRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPaneVerticalSplit, tableScroll);
        splitPaneRight.setResizeWeight(0.66d);

        JScrollPane tilemapsListScroll = new JScrollPane(tilemapsList);
        tilemapsListScroll.addMouseWheelListener(zoomWheelListener);

        JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tilemapsListScroll, splitPaneRight);
        splitPaneLeft.setResizeWeight(0.33d);

        add(splitPaneLeft, BorderLayout.CENTER);

        observer = (eventType, message) -> {
            if (eventType == EventType.TILEMAP_SELECTED && tileMapPanelData.getTileMap() != null) {
                TileMap selectedTileMap = tileMapPanelData.getTileMap();
                com.ostsoft.games.jsm.editor.common.list.EventListModel<Tile> tilemapModel = new com.ostsoft.games.jsm.editor.common.list.EventListModel<>(selectedTileMap.getTiles(), editorData, EventType.TILEMAP_UPDATED);
                currentTileMapList.setModel(tilemapModel);
                if (selectedTileMap instanceof HTileMap) {
                    currentTileMapList.setCols(((HTileMap) selectedTileMap).getSizeX());
                    currentTileMapList.fixRowCountForVisibleColumns();
                }
            }
            else if (eventType == EventType.TILE_UPDATED || eventType == EventType.TILEMAP_UPDATED || eventType == EventType.PALETTE_UPDATED) {
                refreshList(currentTileMapList);
                refreshList(this.tilemapsList);
            }
        };
        editorData.addObserver(observer);
        editorData.addObserver(table);


        JMenuItem importTiles = new JMenuItem("Import tiles");
        importTiles.addActionListener(e -> {
            if (tileMapPanelData.getTiles() != null && tileMapPanelData.getPalette() != null) {
                java.util.List<DnDColor> colors = tileMapPanelData.getPalette().getColors();
                List<Tile> tiles = tileMapPanelData.getTiles();
                TileUtil.importTiles(editorData, colors, tiles);
                refreshList(tileList);
            }
        });
        mnTools.add(importTiles);

        JMenuItem exportTiles = new JMenuItem("Export tiles");
        exportTiles.addActionListener(e -> {
            java.util.List<Tile> tiles = tileMapPanelData.getTiles();
            if (tiles != null && tileMapPanelData.getPalette() != null) {
                TileUtil.exportTiles(tiles, tileMapPanelData.getPalette());
            }
            else {
                editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "You need to select a palette to export");
            }
        });
        mnTools.add(exportTiles);


        if (tileMapPanelData.getTileMaps().size() > 0) {
            tilemapsList.setSelectedIndex(0);
        }
        if (tileMapPanelData.getPalette() != null) {
            for (int i = 0; i < paletteBox.getModel().getSize(); i++) {
                if (paletteBox.getModel().getElementAt(i) == tileMapPanelData.getPalette()) {
                    paletteBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        else {
            paletteBox.setSelectedIndex(0);
        }
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        super.handlePanelEvent(panelEvent);

        switch (panelEvent) {
            case SCALE:
                refreshList(tilemapsList);
                refreshList(currentTileMapList);
                refreshList(tileList);
                break;

            case DELETE:
                UUID uuid = UUID.randomUUID();
                if (currentTileMapList.getModel() instanceof EventListModel) {
                    int[] selectedIndices = currentTileMapList.getSelectedIndices();
                    while (selectedIndices.length > 0) {
                        ((EventListModel) currentTileMapList.getModel()).remove(uuid, selectedIndices[0]);
                        selectedIndices = currentTileMapList.getSelectedIndices();
                    }
                }
                break;

            case CLOSEPANEL:
                editorData.removeObserver(observer);
                editorData.removeObserver(table);
                break;

            default:
                editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "Unsupported action " + panelEvent.name());
        }
    }


    private void refreshList(VerticalWrapList list) {
        list.fixRowCountForVisibleColumns();

        if (list.getCellRenderer() instanceof CustomListRenderer) {
            ((CustomListRenderer) list.getCellRenderer()).clearCache();
        }

        if (list.getModel() instanceof com.ostsoft.games.jsm.editor.common.list.ListModel) {
            ((com.ostsoft.games.jsm.editor.common.list.ListModel) list.getModel()).update();
        }

//        list.repaint();
//        else if (list.getModel() instanceof editor.common.list.ListModel) {
//            ((editor.common.list.ListModel) list.getModel());
//        }
    }
}
