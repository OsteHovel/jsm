package com.ostsoft.games.jsm.editor.common.list;

import javax.swing.*;
import java.util.List;

public class ListModel<T> extends AbstractListModel<T> {
    protected final List<T> list;

    public ListModel(List<T> list) {
        this.list = list;
    }

    public void update() {
        fireContentsChanged(this, 0, getSize());
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public T getElementAt(int index) {
        return list.get(index);
    }

    public void add(int index, T value) {
        list.add(index, value);
        fireIntervalAdded(this, index, index);
    }

    public void remove(int index) {
        list.remove(index);
        fireIntervalRemoved(this, index, index);
    }

}