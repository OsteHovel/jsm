package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.Field;

public class EnumRow extends Row {
    private final Object object;
    private final String fieldName;

    public EnumRow(EditorData table, Object[] columns, Object object, String fieldName, EventType eventType) {
        super(table, columns, eventType);
        this.object = object;
        this.fieldName = fieldName;
    }

    @Override
    public void setValue(Object valueAt) {
        if (object == null) {
            super.setValue(valueAt);
            return;
        }

        try {
            Class<?> c = object.getClass();
            Field field = c.getField(fieldName);
            if (value instanceof Enum) {
                Enum anEnum = (Enum) value;
                field.set(object, getValue(valueAt, anEnum));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Object valueAt) {
        try {
            if (value instanceof Enum) {
                Enum anEnum = (Enum) value;
                getValue(valueAt, anEnum);
            }
            else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update() {
        try {

            Class<?> c = object.getClass();
            Field field = c.getField(fieldName);
            Enum anEnum1 = (Enum) field.get(object);

            return update(anEnum1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Enum getValue(Object valueAt, Enum anEnum) {
        return Enum.valueOf(anEnum.getClass(), String.valueOf(valueAt));
    }

    public Object getObject() {
        return object;
    }
}
