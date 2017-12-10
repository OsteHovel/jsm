package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;

public class StaticStringRow extends Row {
    private final String s;

    public StaticStringRow(EditorData table, Object[] columns, String s) {
        super(table, columns, null);
        this.s = s;
    }

    @Override
    public void setValue(Object valueAt) {
    }

    @Override
    public boolean update() {
        return update(s);
    }

    @Override
    public boolean validate(Object valueAt) {
        return false;
    }
}
