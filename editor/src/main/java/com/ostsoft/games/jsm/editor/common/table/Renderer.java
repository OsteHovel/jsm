package com.ostsoft.games.jsm.editor.common.table;

import com.ostsoft.games.jsm.editor.common.table.columns.BooleanMethodRow;
import com.ostsoft.games.jsm.editor.common.table.columns.BooleanRow;
import com.ostsoft.games.jsm.editor.common.table.columns.Row;
import com.ostsoft.games.jsm.editor.common.table.columns.TileRow;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class Renderer implements TableCellRenderer {

    private final JButton jButton = new JButton("...");

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table.getModel() instanceof DynamicModel) {
            Row rowObject = ((DynamicModel) table.getModel()).getColumn(table.convertRowIndexToModel(row));
            if (column >= rowObject.getColumns().length) {
                if (rowObject instanceof BooleanRow || rowObject instanceof BooleanMethodRow) {
                    return table.getDefaultRenderer(Boolean.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
                else if (rowObject instanceof TileRow) {
                    return jButton;
                }
            }
        }
        return new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
