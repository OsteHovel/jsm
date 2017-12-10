package com.ostsoft.games.jsm.editor.samus;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.table.EditorTable;
import com.ostsoft.games.jsm.editor.common.table.columns.VariableRow;
import com.ostsoft.games.jsm.var.Variable;

import java.util.List;

public class VariableTable extends EditorTable implements Observer {

    public VariableTable(final EditorData editorData, List<Variable> variables) {
        super(editorData);
        for (Variable variable : variables) {
            model.add(new VariableRow(editorData, new String[]{variable.getName()}, variable, EventType.VARIABLE_UPDATE));
        }
        model.update();
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (cellEditor != null) {
            cellEditor.cancelCellEditing();
        }

        model.update();
    }
}
