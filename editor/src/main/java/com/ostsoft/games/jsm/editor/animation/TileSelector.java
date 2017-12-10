package com.ostsoft.games.jsm.editor.animation;

import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.list.ListModel;
import com.ostsoft.games.jsm.editor.tilemap.TileUtil;
import com.ostsoft.games.jsm.editor.tilemap.VerticalWrapList;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class TileSelector extends AbstractCellEditor implements TableCellEditor {
    private final AnimationPanelData animationPanelData;
    private final JFrame jFrame;
    private final VerticalWrapList<Tile> tileList = new VerticalWrapList<>(16);

    private final SelectAction selectAction = new SelectAction();
    private final CancelAction cancelAction = new CancelAction();

    public TileSelector(EditorData editorData, AnimationPanelData animationPanelData) {
        this.animationPanelData = animationPanelData;
        jFrame = new JFrame("jSM - Tile selector");
        jFrame.setSize(700, 400);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setLayout(new BorderLayout(0, 0));

        com.ostsoft.games.jsm.editor.common.list.ListModel<Tile> tileListModel = new ListModel<>(animationPanelData.getAnimationFrame().getTiles());
        tileList.setSelectionForeground(new Color(tileList.getSelectionBackground().getRed(), tileList.getSelectionBackground().getGreen(), tileList.getSelectionBackground().getBlue(), 128));
        tileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tileList.setModel(tileListModel);
        tileList.setCellRenderer(new CustomListRenderer<>(animationPanelData, true, false));
        tileList.setBackground(Color.BLACK);
        tileList.setSelectedIndex(animationPanelData.getSelectEntry().getTileIndex());
        jFrame.add(new JScrollPane(tileList), BorderLayout.CENTER);
        tileList.ensureIndexIsVisible(animationPanelData.getSelectEntry().getTileIndex());

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(new JButton(selectAction));
        horizontalBox.add(new JButton(cancelAction));
        jFrame.add(horizontalBox, BorderLayout.SOUTH);

        jFrame.setVisible(false);


        jFrame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectAction");
        jFrame.getRootPane().getActionMap().put("selectAction", selectAction);

        tileList.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "selectAction");
        tileList.getActionMap().put("selectAction", selectAction);

        tileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    e.consume();
                    selectAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "selectAction"));
                }
            }
        });


        jFrame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
        jFrame.getRootPane().getActionMap().put("cancelAction", cancelAction);

        tileList.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
        tileList.getActionMap().put("cancelAction", cancelAction);


        JMenuBar jMenuBar = new JMenuBar();
        jFrame.setJMenuBar(jMenuBar);
        JMenu mnTools = new JMenu("Tools");
        jMenuBar.add(mnTools);
        {

            JMenuItem importTiles = new JMenuItem("Import tiles");
            importTiles.addActionListener(e -> {
                TileUtil.importTiles(editorData, animationPanelData.getPalette().getColors(), animationPanelData.getAnimationFrame().getTiles());
            });
            mnTools.add(importTiles);

            JMenuItem exportTiles = new JMenuItem("Export tiles");
            exportTiles.addActionListener(e -> {
                TileUtil.exportTiles(animationPanelData.getAnimationFrame().getTiles(), animationPanelData.getPalette());
            });
            mnTools.add(exportTiles);
        }
    }

    @Override
    public boolean stopCellEditing() {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        return super.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        super.cancelCellEditing();
    }

    @Override
    public Object getCellEditorValue() {
        return tileList.getSelectedIndex();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JButton open = new JButton("...");
        open.addActionListener(e -> {
            jFrame.setVisible(true);
        });
        return open;
    }

    private class SelectAction extends AbstractAction {

        public SelectAction() {
            putValue(NAME, "Select");
        }

        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        }

    }

    private class CancelAction extends AbstractAction {
        public CancelAction() {
            putValue(NAME, "Cancel");
        }

        public void actionPerformed(ActionEvent e) {
            fireEditingCanceled();
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        }
    }
}
