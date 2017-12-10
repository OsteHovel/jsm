package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

public abstract class Row {
    protected final EditorData editorData;
    protected final Object[] columns;
    protected final EventType eventType;
    protected Object value = null;

    public Row(EditorData editorData, Object[] columns, EventType eventType) {
        this.editorData = editorData;
        this.columns = columns;
        this.eventType = eventType;
    }

    public abstract boolean update();

    protected boolean update(Object object) {
        boolean isEquals = false;
        if (value != null && object != null) {
            isEquals = value.equals(object);
        }

        value = object;
        return !isEquals;
    }

    public Object[] getColumns() {
        return columns;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object valueAt) {
        value = valueAt;
    }

    public EventType getEventType() {
        return eventType;
    }

    public abstract boolean validate(Object valueAt);
}
