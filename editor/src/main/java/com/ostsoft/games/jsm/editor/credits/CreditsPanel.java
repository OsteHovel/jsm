package com.ostsoft.games.jsm.editor.credits;

import com.ostsoft.games.jsm.common.TileMap;
import com.ostsoft.games.jsm.credits.BlankLine;
import com.ostsoft.games.jsm.credits.CreditLine;
import com.ostsoft.games.jsm.credits.Credits;
import com.ostsoft.games.jsm.credits.CreditsTileMap;
import com.ostsoft.games.jsm.credits.TextLine;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.list.EventListModel;
import com.ostsoft.games.jsm.editor.common.list.ListModel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;

public class CreditsPanel extends EditorPanel {
    private final CreditsPanelData creditsPanelData;
    private final Observer observer;
    private final CreditsList<CreditLine> creditsList;
    private final EventListModel<CreditLine> model;
    private final CreditsTable table;
    private final ListModel<CreditLine> chooseModel;
    private final JList<CreditLine> chooseList;


    public CreditsPanel(EditorData editorData) {
        super(editorData, new CreditsPanelData(editorData));
        this.creditsPanelData = (CreditsPanelData) panelData;
        Credits credits = editorData.getSuperMetroid().getCredits();

        setLayout(new BorderLayout(0, 0));
        model = new EventListModel<>(credits.getCreditsMap(), editorData, EventType.CREDITSLINES_UPDATED);

        java.util.List<CreditLine> tileList = new ArrayList<>();
        for (TileMap tileMap : credits.getTileMaps()) {
            tileList.add(new TextLine((CreditsTileMap) tileMap));
        }

        chooseModel = new ListModel<>(tileList);
        creditsList = new CreditsList<>(creditsPanelData);
        creditsList.setBackground(Color.BLACK);
        creditsList.setModel(model);

        ActionMap map = creditsList.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());

        creditsList.addListSelectionListener(e -> {
            int selectedIndex = creditsList.getSelectedIndex();
            if (selectedIndex < model.getSize() && selectedIndex >= 0) {
                Object elementAt = model.getElementAt(selectedIndex);
                if (elementAt != null) {
                    if (creditsPanelData.getSelectedCreditsLine() != elementAt) {
                        creditsPanelData.setSelectedCreditsLine((CreditLine) elementAt);
                        editorData.fireEvent(EventType.CREDITSLINE_SELECTED);
                    }
                }
            }

        });
        JScrollPane listScrollPane = new JScrollPane(creditsList);
        listScrollPane.addMouseWheelListener(zoomWheelListener);

        chooseList = new CreditsList<>(creditsPanelData);
        chooseList.setBackground(Color.BLACK);
        chooseList.setModel(chooseModel);

        JScrollPane chooseScrollPane = new JScrollPane(chooseList);
        chooseScrollPane.addMouseWheelListener(zoomWheelListener);

        JSplitPane splitPaneLists = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, chooseScrollPane);
        splitPaneLists.addMouseWheelListener(zoomWheelListener);
        splitPaneLists.setResizeWeight(0.5d);

        Box verticalBox = Box.createVerticalBox();

        JButton btnInsertBlankLine = new JButton("Insert blank line");
        btnInsertBlankLine.addActionListener(e ->

                {
                    EventListModel<CreditLine> model = (EventListModel<CreditLine>) creditsList.getModel();
                    int selectedIndex = creditsList.getSelectedIndex();
                    if (selectedIndex < 0) {
                        selectedIndex = 0;
                    }
                    model.add(UUID.randomUUID(), selectedIndex, new BlankLine(1));
                }

        );
        verticalBox.add(btnInsertBlankLine);

//        JButton btnEditTilemap = new JButton("Edit tilemap");
//        btnEditTilemap.addActionListener(e ->
//
//                {
//                    JOptionPane.showMessageDialog(this, "Not yet");
//
//                }
//
//        );
//        verticalBox.add(btnEditTilemap);

        table = new CreditsTable(editorData, creditsPanelData);


        verticalBox.add(table);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPaneLists, verticalBox);
        splitPane.addMouseWheelListener(zoomWheelListener);
        splitPane.addMouseWheelListener(zoomWheelListener);
        splitPane.setResizeWeight(0.85d);

        add(splitPane, BorderLayout.CENTER);


        observer = (eventType, message) ->
        {
            if (eventType == EventType.CREDITSLINE_UPDATED) {
                model.update();
                ListCellRenderer cellRenderer = creditsList.getCellRenderer();
                if (cellRenderer instanceof CustomListRenderer) {
                    ((CustomListRenderer) cellRenderer).clearCache();
                }
            }
            else if (eventType == EventType.CREDITSLINES_UPDATED) {
                model.update();
            }
            else if (eventType == EventType.TILEMAP_UPDATED || eventType == EventType.TILE_UPDATED || eventType == EventType.PALETTE_UPDATED) {
                updateModel();
            }

        };

        editorData.addObserver(observer);
        editorData.addObserver(table);

        JMenuItem exportCredits = new JMenuItem("Export");
        exportCredits.addActionListener(e -> FileChooserUtil.saveImageAsFile(getImage(), "credits.png"));
        mnTools.add(exportCredits);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        super.handlePanelEvent(panelEvent);

        switch (panelEvent) {
            case ZOOM_IN:
            case ZOOM_OUT:
                break;

            case SCALE:
                updateModel();
                break;

            case DELETE:
                UUID uuid = UUID.randomUUID();
                int[] selectedIndices = creditsList.getSelectedIndices();
                while (selectedIndices.length > 0) {
                    model.remove(uuid, selectedIndices[0]);
                    selectedIndices = creditsList.getSelectedIndices();
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

    private void updateModel() {
        model.update();
        if (creditsList.getCellRenderer() instanceof CustomListRenderer) {
            ((CustomListRenderer) creditsList.getCellRenderer()).clearCache();
        }

        chooseModel.update();
        if (chooseList.getCellRenderer() instanceof CustomListRenderer) {
            ((CustomListRenderer) chooseList.getCellRenderer()).clearCache();
        }
    }

    private BufferedImage getImage() {
        java.util.List<CreditLine> creditsMap = editorData.getSuperMetroid().getCredits().getCreditsMap();

        int height = 0;
        for (CreditLine creditLine : creditsMap) {
            if (creditLine instanceof BlankLine) {
                height += (((BlankLine) creditLine).numberOfLines - 1) * 8;
            }
            height += 8;
        }
        if (height <= 0) {
            height = 8;
        }

        BufferedImage image = new BufferedImage(32 * 8, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        int y = 0;
        for (CreditLine creditLine : creditsMap) {
            if (creditLine instanceof TextLine) {
                g.drawImage(creditLine.getImage(), 0, y, null);
            }
            else if (creditLine instanceof BlankLine) {
                y += (((BlankLine) creditLine).numberOfLines - 1) * 8;
            }
            y += 8;
        }

        g.dispose();
        return image;
    }

}
