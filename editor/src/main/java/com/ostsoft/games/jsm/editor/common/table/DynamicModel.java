package com.ostsoft.games.jsm.editor.common.table;


import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.TileSelector;
import com.ostsoft.games.jsm.editor.common.observer.command.SetRowValueCommand;
import com.ostsoft.games.jsm.editor.common.table.columns.BooleanMethodRow;
import com.ostsoft.games.jsm.editor.common.table.columns.BooleanRow;
import com.ostsoft.games.jsm.editor.common.table.columns.EnumRow;
import com.ostsoft.games.jsm.editor.common.table.columns.Row;
import com.ostsoft.games.jsm.editor.common.table.columns.TileRow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.util.ArrayList;

public class DynamicModel extends AbstractTableModel {
    private final EditorData editorData;
    private final String[] columnNames;
    private final int columnCount;
    private final ArrayList<Row> rows = new ArrayList<>();

    public DynamicModel(EditorData editorData, String[] columnNames) {
        this.editorData = editorData;
        this.columnNames = columnNames;
        this.columnCount = columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumnCount() - 1 == columnIndex;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex >= rows.get(rowIndex).getColumns().length) {
            // Data column
            return rows.get(rowIndex).getValue();
        }
        return rows.get(rowIndex).getColumns()[columnIndex];
    }

    public Row getColumn(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        if (rowIndex < rows.size()) {
            Row row = rows.get(rowIndex);
            if (row.getEventType() != null) {
                SetRowValueCommand setRowValueCommand = new SetRowValueCommand(editorData, row, newValue);
                if (row.validate(newValue)) {
                    editorData.getCommandCenter().executeCommand(setRowValueCommand);
                }
            }
        }
    }

    public void add(Row row) {
        rows.add(row);
        fireTableDataChanged();
    }

    public void update() {
        for (Row row : rows) {
            if (row.update()) {
                fireTableCellUpdated(rows.indexOf(row), columnCount - 1);
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    public TableCellEditor getCellEditor(int row, int column) {
        Row row1 = rows.get(row);
        if (row1 instanceof BooleanRow || row1 instanceof BooleanMethodRow) {
            JCheckBox jCheckBox = new JCheckBox();
            jCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
            return new DefaultCellEditor(jCheckBox);
        }
        else if (row1 instanceof EnumRow) {
            Enum anEnum = (Enum) row1.getValue();
            ArrayList<String> anEnumNames = new ArrayList<>();
            for (Enum anEnum1 : anEnum.getClass().getEnumConstants()) {
                anEnumNames.add(anEnum1.name());
            }
            String[] array = anEnumNames.toArray(new String[anEnumNames.size()]);
            JComboBox<String> stringJComboBox = new JComboBox<>(array);
            stringJComboBox.setSelectedIndex(anEnumNames.indexOf(anEnum.name()));
            return new DefaultCellEditor(stringJComboBox);
        }
        else if (row1 instanceof TileRow) {
            return new TileSelector(editorData, ((TileRow) row1).getAnimationPanelData());
        }
        return new DefaultCellEditor(new JTextField());
    }

    public void clear() {
        rows.clear();
    }
}
