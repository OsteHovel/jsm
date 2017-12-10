package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.Field;

public class StringRow extends Row {
    private final Object o;
    private final String fieldName;

    public StringRow(EditorData table, Object[] columns, Object o, String fieldName, EventType eventType) {
        super(table, columns, eventType);
        this.o = o;
        this.fieldName = fieldName;
    }

    @Override
    public void setValue(Object valueAt) {
        if (o == null) {
            super.setValue(String.valueOf(valueAt));
            return;
        }

        try {
            Class<?> c = o.getClass();
            Field field = c.getField(fieldName);
            field.set(o, String.valueOf(valueAt));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update() {
        try {

            Class<?> c = o.getClass();
            Field field = c.getField(fieldName);
            String object = (String) field.get(o);

            return update(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean validate(Object valueAt) {
        return true;
    }
}
