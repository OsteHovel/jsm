package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IntegerMethodRow extends Row {
    protected final Object o;
    protected final String getMethodName;
    protected final String setMethodName;


    public IntegerMethodRow(EditorData editorData, Object[] columns, Object o, String getMethodName, String setMethodName, EventType eventType) {
        super(editorData, columns, eventType);
        this.o = o;
        this.getMethodName = getMethodName;
        this.setMethodName = setMethodName;
    }

    @Override
    public void setValue(Object valueAt) {
        if (setMethodName != null) {
            try {
                Class<?> c = o.getClass();
                Class[] argTypes = new Class[]{int.class};
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

    @Override
    public boolean update() {
        if (getMethodName != null) {
            try {
                Class<?> c = o.getClass();
                Method method = c.getMethod(getMethodName);
                int object = (int) method.invoke(o);
                if (editorData.getOptions().getNumberBase() == 16) {
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

    public Object getO() {
        return o;
    }

    protected int getValue(Object valueAt) {
        String string = String.valueOf(valueAt);
        return decodeWithoutOctal(string);
    }

    private Integer decodeWithoutOctal(String nm) throws NumberFormatException {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        Integer result;

        if (nm.length() == 0) {
            throw new NumberFormatException("Zero length string");
        }
        char firstChar = nm.charAt(0);
        // Handle sign, if present
        if (firstChar == '-') {
            negative = true;
            index++;
        }
        else if (firstChar == '+') {
            index++;
        }

        // Handle radix specifier, if present
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        }
        else if (nm.startsWith("#", index)) {
            index++;
            radix = 16;
        }
        else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
            index++;
            radix = 10;
        }

        if (nm.startsWith("-", index) || nm.startsWith("+", index)) {
            throw new NumberFormatException("Sign character in wrong position");
        }

        try {
            result = Integer.valueOf(nm.substring(index), radix);
            result = negative ? Integer.valueOf(-result.intValue()) : result;
        } catch (NumberFormatException e) {
            // If number is Integer.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            String constant = negative ? ("-" + nm.substring(index))
                    : nm.substring(index);
            result = Integer.valueOf(constant, radix);
        }
        return result;
    }
}
