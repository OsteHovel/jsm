package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.lang.reflect.Field;

public class IntegerRow extends Row {
    private final Object o;
    private final String fieldName;

    public IntegerRow(EditorData table, Object[] columns, Object o, String fieldName, EventType eventType) {
        super(table, columns, eventType);
        this.o = o;
        this.fieldName = fieldName;
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
    public boolean update() {
        try {
            Class<?> c = o.getClass();
            Field field = c.getField(fieldName);
            int object = (int) field.get(o);

            if (editorData.getOptions().getNumberBase() == 16) {
                return update("0x" + Integer.toHexString(object).toUpperCase());
            }
            else {
                return update(object);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
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

    private int getValue(Object valueAt) {
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
