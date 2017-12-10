package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringMethodRow extends Row {
    private final Object o;
    private final String getMethodName;
    private final String setMethodName;


    public StringMethodRow(EditorData table, Object[] columns, Object o, String getMethodName, String setMethodName, EventType eventType) {
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
                Class[] argTypes = new Class[]{String.class};
                Method method = c.getMethod(setMethodName, argTypes);
                Object object = method.invoke(o, valueAt);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean update() {
        if (getMethodName != null) {
            try {
                Class<?> c = o.getClass();
                Method method = c.getMethod(getMethodName);
                String object = (String) method.invoke(o);

                return update(object);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Object getO() {
        return o;
    }

    @Override
    public boolean validate(Object valueAt) {
        return true;
    }
}
