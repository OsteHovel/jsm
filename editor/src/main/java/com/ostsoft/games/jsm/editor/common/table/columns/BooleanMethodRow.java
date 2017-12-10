package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BooleanMethodRow extends Row {
    private final Object o;
    private final String getMethodName;
    private final String setMethodName;


    public BooleanMethodRow(EditorData table, Object[] columns, Object o, String getMethodName, String setMethodName, EventType eventType) {
        super(table, columns, eventType);
        this.o = o;
        this.getMethodName = getMethodName;
        this.setMethodName = setMethodName;
    }

    @Override
    public void setValue(Object valueAt) {
        if (setMethodName != null) {
            try {
                Class<?> c = o.getClass();
                Class[] argTypes = new Class[]{boolean.class};
                Method method = c.getMethod(setMethodName, argTypes);
                Object object = method.invoke(o, getValue(valueAt));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
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

    private boolean getValue(Object valueAt) {
        return Boolean.parseBoolean(String.valueOf(valueAt));
    }

    @Override
    public boolean update() {
        if (getMethodName != null) {
            try {
                Class<?> c = o.getClass();
                Method method = c.getMethod(getMethodName);
                boolean object = (boolean) method.invoke(o);
                return update(object);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Object getO() {
        return o;
    }
}
