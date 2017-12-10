package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.util.BitHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PointerRow extends IntegerMethodRow {
    private final EditorData editorData;

    public PointerRow(EditorData editorData, Object[] columns, Object o, String getMethodName, String setMethodName, EventType eventType) {
        super(editorData, columns, o, getMethodName, setMethodName, eventType);
        this.editorData = editorData;
    }

    @Override
    public void setValue(Object valueAt) {
        if (!editorData.getOptions().isPointerSnes()) {
            valueAt = BitHelper.pcToSnes(getValue(valueAt));
        }
        super.setValue(valueAt);
    }

    @Override
    public boolean update() {
        if (getMethodName != null) {
            try {
                Class<?> c = o.getClass();
                Method method = c.getMethod(getMethodName);
                int object = (int) method.invoke(o);
                if (!editorData.getOptions().isPointerSnes()) {
                    object = BitHelper.snesToPc(object);
                }

                if (editorData.getOptions().getPointerBase() == 16) {
                    return update("0x" + Integer.toHexString(object).toUpperCase());
                }
                else {
                    return update(object);
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
