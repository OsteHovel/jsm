package com.ostsoft.games.jsm.editor.common.gui;

import com.ostsoft.games.jsm.editor.Editor;

import javax.swing.*;

public class EditorToolBar {
    public static class File extends JToolBar {
        public File(Editor editor) {
            JButton btnOpen = new JButton(editor.getEditorActions().getLoadAction());
            btnOpen.setHideActionText(true);
            add(btnOpen);

            JButton btnSave = new JButton(editor.getEditorActions().getSaveAction());
            btnSave.setHideActionText(true);
            add(btnSave);
        }
    }

    public static class Edit extends JToolBar {
        public Edit(Editor editor) {
            JButton btnUndo = new JButton(editor.getEditorActions().getUndoAction());
            btnUndo.setHideActionText(true);
            add(btnUndo);

            JButton btnRedo = new JButton(editor.getEditorActions().getRedoAction());
            btnRedo.setHideActionText(true);
            add(btnRedo);


            JButton btnCut = new JButton(editor.getEditorActions().getCutAction());
            btnCut.setHideActionText(true);
            add(btnCut);

            JButton btnCopy = new JButton(editor.getEditorActions().getCopyAction());
            btnCopy.setHideActionText(true);
            add(btnCopy);

            JButton btnPaste = new JButton(editor.getEditorActions().getPasteAction());
            btnPaste.setHideActionText(true);
            add(btnPaste);

            JButton btnDelete = new JButton(editor.getEditorActions().getDeleteAction());
            btnDelete.setHideActionText(true);
            add(btnDelete);
        }
    }
}
