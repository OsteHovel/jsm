package com.ostsoft.games.jsm.editor.common.gui;

import com.ostsoft.games.jsm.editor.Editor;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class EditorStatusBar extends JPanel implements Observer {
    private final Editor editor;
    private final JLabel statusLabel;

    public EditorStatusBar(Editor editor) {
        super();
        this.editor = editor;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusLabel);
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (message != null) {
            statusLabel.setText(message);
        }
    }
}
