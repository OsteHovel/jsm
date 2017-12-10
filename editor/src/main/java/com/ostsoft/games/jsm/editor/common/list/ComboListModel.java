package com.ostsoft.games.jsm.editor.common.list;

import javax.swing.*;
import java.util.List;

public class ComboListModel<T> extends ListModel<T> implements ComboBoxModel<T> {
    private Object selectedItem;

    public ComboListModel(List<T> list) {
        super(list);
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }
}