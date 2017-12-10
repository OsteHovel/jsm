package com.ostsoft.games.jsm.editor.common.table;

import com.ostsoft.games.jsm.editor.EditorData;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class EditorTable extends JTable {
    protected final DynamicModel model;

    public EditorTable(EditorData editorData) {
        model = new DynamicModel(editorData, new String[]{"Property", "Value"});
        setModel(model);

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultRenderer(Object.class, new com.ostsoft.games.jsm.editor.common.table.Renderer());
        setAutoCreateRowSorter(true);
    }


    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return getModel().getCellEditor(convertRowIndexToModel(row), convertColumnIndexToModel(column));
    }

    @Override
    public DynamicModel getModel() {
        return model;
    }
}
