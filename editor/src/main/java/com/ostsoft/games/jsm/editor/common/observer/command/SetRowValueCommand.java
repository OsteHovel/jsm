package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.table.columns.Row;

public class SetRowValueCommand extends Command {

    private final EditorData editorData;
    private final Row row;
    private final Object oldValue, newValue;

    public SetRowValueCommand(EditorData editorData, Row row, Object newValue) {
        this.editorData = editorData;
        this.row = row;
        this.oldValue = row.getValue();
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        row.setValue(newValue);
        editorData.fireEvent(row.getEventType());
    }

    @Override
    public void undo() {
        row.setValue(oldValue);
        editorData.fireEvent(row.getEventType());
    }

    @Override
    String getDescription() {
        return "Updated";
    }

}
