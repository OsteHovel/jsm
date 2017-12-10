package com.ostsoft.games.jsm.editor.common.gui;

import com.ostsoft.games.jsm.editor.EditorData;

import javax.swing.*;

public class PopupMenu extends JPopupMenu {
    public PopupMenu(EditorData editorData) {
        add(new JMenuItem(editorData.getEditorActions().getInsertAction()));
        add(new JMenuItem(editorData.getEditorActions().getRemoveAction()));
        add(new JSeparator());

        add(new JMenuItem(editorData.getEditorActions().getCutAction()));
        add(new JMenuItem(editorData.getEditorActions().getCopyAction()));
        add(new JMenuItem(editorData.getEditorActions().getPasteAction()));
        add(new JMenuItem(editorData.getEditorActions().getDeleteAction()));
        add(new JSeparator());

        add(new JMenuItem(editorData.getEditorActions().getPropertiesAction()));
    }


}
