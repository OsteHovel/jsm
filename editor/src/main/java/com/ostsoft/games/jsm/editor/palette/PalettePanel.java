package com.ostsoft.games.jsm.editor.palette;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.list.EventListItemTransferHandler;
import com.ostsoft.games.jsm.editor.common.list.EventListModel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.observer.command.AddPaletteColor;
import com.ostsoft.games.jsm.editor.common.observer.command.ChangePaletteColor;
import com.ostsoft.games.jsm.editor.common.observer.command.RemovePaletteColor;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.editor.tilemap.VerticalWrapList;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.palette.PaletteManager;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class PalettePanel extends EditorPanel {
    private final PalettePanelData palettePanelData;
    private final Observer observer;
    private final Observer observerColorChooser;
    private final VerticalWrapList<DnDColor> colorList;
    private final JButton setColor = new JButton("Set color");

    public PalettePanel(EditorData editorData) {
        super(editorData, new PalettePanelData(editorData));
        palettePanelData = (PalettePanelData) panelData;
        setLayout(new BorderLayout(0, 0));

        PaletteManager paletteManager = editorData.getSuperMetroid().getPaletteManager();
        JList<Palette> paletteList = new JList<>(new EventListModel<>(paletteManager.getPalettes(), editorData, EventType.PALETTES_UPDATED));
        paletteList.addListSelectionListener(e -> {
            Palette selectedValue = paletteList.getSelectedValue();
            if (selectedValue != null) {
                palettePanelData.setPalette(selectedValue);
                palettePanelData.setSelectedColor(null);
                editorData.fireEvent(EventType.PALETTE_SELECTED);
            }
        });
        //paletteList.setCellRenderer(new CustomListRenderer<>(panelData));


        colorList = new VerticalWrapList<>(16);
        colorList.setSelectionForeground(new Color(colorList.getSelectionBackground().getRed(), colorList.getSelectionBackground().getGreen(), colorList.getSelectionBackground().getBlue(), 128));
        colorList.setCellRenderer(new CustomListRenderer<>(palettePanelData, true, false));
        colorList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        colorList.setTransferHandler(new EventListItemTransferHandler<>(TransferHandler.COPY_OR_MOVE));
        colorList.setDropMode(DropMode.INSERT);
        colorList.setDragEnabled(true);
        ActionMap map = colorList.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());

        colorList.addListSelectionListener(e -> {
            Object selectedValue = colorList.getSelectedValue();
            if (selectedValue != null) {
                palettePanelData.setSelectedColor((DnDColor) selectedValue);
                editorData.fireEvent(EventType.PALETTE_COLOR_SELECTED);
            }
        });

//        colorList.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int selectedIndex = colorList.getSelectedIndex();
//                if (e.getClickCount() > 1 && selectedIndex >= 0) {
//                    Color newColor = JColorChooser.showDialog(
//                            PalettePanel.this,
//                            "Choose color",
//                            (Color) colorList.getSelectedValue());
//
//                    if (newColor != null) {
//                        if (colorList.getModel() instanceof com.ostsoft.games.jsm.editor.common.list.EventListModel) {
//                            com.ostsoft.games.jsm.editor.common.list.EventListModel<DnDColor> model = (EventListModel<DnDColor>) colorList.getModel();
//                            UUID uuid = UUID.randomUUID();
//                            model.remove(uuid, selectedIndex);
//                            model.add(uuid, selectedIndex, new DnDColor(newColor));
//                        }
//                    }
//                    e.consume();
//                }
//            }
//        });


        observer = (eventType, message) -> {
            if (eventType == EventType.PALETTE_SELECTED && palettePanelData.getPalette() != null) {
                Palette selectedPalette = palettePanelData.getPalette();
                com.ostsoft.games.jsm.editor.common.list.EventListModel<DnDColor> colorModel = new com.ostsoft.games.jsm.editor.common.list.EventListModel<>(selectedPalette.getColors(), editorData, EventType.PALETTE_UPDATED);
                colorList.setModel(colorModel);
                refreshList(colorList);
            }
            else if (eventType == EventType.PALETTE_UPDATED) {
                refreshList(colorList);
            }
        };
        editorData.addObserver(observer);

        JScrollPane paletteListScrollPane = new JScrollPane(paletteList);
        paletteListScrollPane.addMouseWheelListener(zoomWheelListener);

        JScrollPane colorScrollPane = new JScrollPane(colorList);
        colorScrollPane.addMouseWheelListener(zoomWheelListener);

        JColorChooser colorChooser = new JColorChooser();
        observerColorChooser = (eventType, message) -> {
            if (eventType == EventType.PALETTE_SELECTED || eventType == EventType.PALETTE_COLOR_SELECTED) {
                if (palettePanelData.getSelectedColor() != null) {
                    setColor.setEnabled(true);
                    colorChooser.setColor(palettePanelData.getSelectedColor());
                }
                else {
                    setColor.setEnabled(false);
                }
            }
        };
        editorData.addObserver(observerColorChooser);

        Box verticalBox = Box.createVerticalBox();
        setColor.setEnabled(false);
        setColor.setAlignmentX(Component.CENTER_ALIGNMENT);
        setColor.setMaximumSize(new Dimension(5000, 25));
        setColor.addActionListener(e1 -> {
            if (palettePanelData.getSelectedColor() != null) {
                int selectedIndex = palettePanelData.getPalette().getColors().indexOf(palettePanelData.getSelectedColor());
                if (selectedIndex >= 0) {
                    DnDColor newColor = new DnDColor(colorChooser.getColor());
                    palettePanelData.setSelectedColor(newColor);
                    com.ostsoft.games.jsm.editor.common.list.EventListModel<DnDColor> model = (EventListModel<DnDColor>) colorList.getModel();
                    UUID uuid = UUID.randomUUID();
                    model.remove(uuid, selectedIndex);
                    model.add(uuid, selectedIndex, newColor);
                }
            }
        });

        verticalBox.add(setColor);
        verticalBox.add(colorChooser);

        Box verticalBox1 = Box.createVerticalBox();
        verticalBox1.add(new JLabel("Add slider for number of colums, and max colors and a table"));

        JSplitPane jSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, colorScrollPane, verticalBox);
        jSplitPane2.setResizeWeight(0.9d);

        JSplitPane jSplitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, paletteListScrollPane, verticalBox1);
        jSplitPane2.setResizeWeight(0.9d);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jSplitPane3, jSplitPane2);
        jSplitPane.setResizeWeight(0.3d);
        add(jSplitPane, BorderLayout.CENTER);

        paletteList.setSelectedIndex(0);


        JMenuItem importPalette = new JMenuItem("Import");
        importPalette.addActionListener(e -> {
            if (palettePanelData.getPalette() != null) {
                java.util.List<DnDColor> newColors = PaletteUtil.importPalette();
                if (newColors != null) {
                    java.util.List<DnDColor> colors = palettePanelData.getPalette().getColors();
                    UUID uuid = UUID.randomUUID();

                    for (int i = 0; i < newColors.size(); i++) {
                        if (i >= colors.size()) {
                            editorData.getCommandCenter().executeCommand(new AddPaletteColor(uuid, editorData, palettePanelData.getPalette(), newColors.get(i)));
                        }
                        else {
                            editorData.getCommandCenter().executeCommand(new ChangePaletteColor(uuid, editorData, palettePanelData.getPalette(), i, newColors.get(i)));
                        }
                    }
                    if (newColors.size() < colors.size()) {
                        for (int i = colors.size() - 1; i >= newColors.size(); i--) {
                            editorData.getCommandCenter().executeCommand(new RemovePaletteColor(uuid, editorData, palettePanelData.getPalette(), i));
                        }
                    }
                }
            }
        });
        mnTools.add(importPalette);

        JMenuItem exportPalette = new JMenuItem("Export");
        exportPalette.addActionListener(e -> {
            if (palettePanelData.getPalette() != null) {
                PaletteUtil.exportPalette(palettePanelData.getPalette());
            }
            else {
                editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "You need to select a palette first");
            }
        });
        mnTools.add(exportPalette);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        super.handlePanelEvent(panelEvent);
        switch (panelEvent) {
            case SCALE:
                refreshList(colorList);
                break;

            case DELETE:
                UUID uuid = UUID.randomUUID();
                if (colorList.getModel() instanceof EventListModel) {
                    int[] selectedIndices = colorList.getSelectedIndices();
                    while (selectedIndices.length > 0) {
                        ((EventListModel) colorList.getModel()).remove(uuid, selectedIndices[0]);
                        selectedIndices = colorList.getSelectedIndices();
                    }
                }
                break;

            case CLOSEPANEL:
                editorData.removeObserver(observer);
                editorData.removeObserver(observerColorChooser);
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
    }
}

