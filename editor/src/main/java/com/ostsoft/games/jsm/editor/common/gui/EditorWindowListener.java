package com.ostsoft.games.jsm.editor.common.gui;

import com.ostsoft.games.jsm.editor.Editor;

import java.awt.event.WindowEvent;

public class EditorWindowListener implements java.awt.event.WindowListener {
    private final Editor editor;

    public EditorWindowListener(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        editor.cleanup();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
