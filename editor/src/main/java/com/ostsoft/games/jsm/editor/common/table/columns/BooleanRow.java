package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.Field;

public class BooleanRow extends Row {
    private final Object o;
    private final String fieldName;

    public BooleanRow(EditorData table, Object[] columns, Object o, String fieldName, EventType eventType) {
        super(table, columns, eventType);
        this.o = o;
        this.fieldName = fieldName;
        value = false;
    }

    @Override
    public void setValue(Object valueAt) {
        if (o == null) {
            super.setValue(getValue(valueAt));
            return;
        }

        try {
            Class<?> c = o.getClass();
            Field field = c.getField(fieldName);
            field.set(o, getValue(valueAt));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Object valueAt) {
        try {
            getValue(valueAt);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update() {
        try {

            Class<?> c = o.getClass();
            Field field = c.getField(fieldName);
            boolean object = (boolean) field.get(o);

            return update(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean getValue(Object valueAt) {
        return Boolean.parseBoolean(String.valueOf(valueAt));
    }
}
