package com.ostsoft.games.jsm.editor;

import com.ostsoft.games.jsm.RoomList;
import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.editor.common.About;
import com.ostsoft.games.jsm.editor.common.gui.EditorActions;
import com.ostsoft.games.jsm.editor.common.gui.EditorMenu;
import com.ostsoft.games.jsm.editor.common.gui.EditorStatusBar;
import com.ostsoft.games.jsm.editor.common.gui.EditorToolBar;
import com.ostsoft.games.jsm.editor.common.gui.EditorWindowListener;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.panel.EditorTabbedPane;
import com.ostsoft.games.jsm.editor.common.panel.MultiBorderLayout;
import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.options.Options;
import com.ostsoft.games.jsm.editor.samus.PosePanel;
import com.ostsoft.games.jsm.util.ByteStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Collections;

public class Editor implements Observer {
    private final JFrame jFrame = new JFrame();
    private final EditorTabbedPane tabbedPane = new EditorTabbedPane();
    private final EditorFile editorFile = new EditorFile(this);
    private final EditorStatusBar editorStatusBar = new EditorStatusBar(this);
    private final EditorActions editorActions = new EditorActions(this);

    private final boolean slaveEditor;
    private EditorData editorData;

    public Editor(EditorData editorData) {
        this.editorData = editorData;
        this.slaveEditor = (editorData != null);

        jFrame.getContentPane().setLayout(new MultiBorderLayout());
        jFrame.getContentPane().add(tabbedPane);
        jFrame.setTitle("jSM " + About.VERSION + " " + About.SPECIAL + (slaveEditor ? " - linked window" : "") + (About.DEBUG ? " (DEBUG MODE)" : ""));
        jFrame.setSize(1024, 768);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jFrame.setJMenuBar(new EditorMenu(this));
        jFrame.getContentPane().add(new EditorToolBar.File(this), BorderLayout.NORTH);
        jFrame.getContentPane().add(new EditorToolBar.Edit(this), BorderLayout.NORTH);
        jFrame.getContentPane().add(editorStatusBar, BorderLayout.SOUTH);
        jFrame.addWindowListener(new EditorWindowListener(this));

        editorActions.updateActions();
        if (editorData != null) {
            editorData.addObserver(this);
            editorData.addObserver(editorActions);
            editorData.addObserver(editorStatusBar);
            openDefaultTabs();
        }

        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Editor editor = new Editor(null);
        Options options = new Options();
        options.load();
        if (options.isOpenLastFile()) {
            java.io.File file = new java.io.File(options.getLastFile());
            if (file.isFile() && file.exists()) {
                editor.getEditorFile().load(file);
            }
            else {
                editor.getEditorFile().load(false);
            }
        }
        else {
            editor.getEditorFile().load(false);
        }
    }

    public void load(ByteStream stream) {
        if (editorData != null) {
            editorData.fireEvent(EventType.CLOSE);
        }

        SuperMetroid superMetroid = new SuperMetroid(stream,
                SuperMetroid.Flag.PALETTES.value | SuperMetroid.Flag.SAMUS.value | SuperMetroid.Flag.CREDITS.value |
                        SuperMetroid.Flag.ANIMATEDTILES.value | SuperMetroid.Flag.LOGO.value | SuperMetroid.Flag.PHYSICS.value,
                RoomList.getRoomList(),
                Collections.emptyList()
        );

        if (superMetroid.getHeader().isPAL()) {
            ErrorUtil.displayErrorBox("This is PAL rom, they are not supported.");
            return;
        }


        editorData = new EditorData(editorActions, superMetroid);
        editorData.addObserver(this);
        editorData.addObserver(editorActions);
        editorData.addObserver(editorStatusBar);
        editorActions.updateActions();

        editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "Loaded");
        openDefaultTabs();
    }

    private void openDefaultTabs() {
        tabbedPane.addTab("Pose", new PosePanel(editorData));
//        tabbedPane.addTab("Animated Tiles", new AnimatedTilesPanel(editorData));
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (eventType == EventType.LOAD) {
            if (!slaveEditor) {
                getEditorFile().load(false);
            }
        }
        else if (eventType == EventType.SAVE) {
            if (!slaveEditor) {
                editorData.getSuperMetroid().save();
                editorFile.save(false);
            }
        }
        else if (eventType == EventType.SAVE_AS) {
            if (!slaveEditor) {
                editorData.getSuperMetroid().save();
                editorFile.save(true);
            }
        }
        else if (eventType == EventType.CLOSE) {
            cleanup();
            if (slaveEditor) {
                jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
            }
        }
        else if (eventType == EventType.EXIT) {
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        }
    }

    public void cleanup() {
        while (tabbedPane.getTabCount() > 0) {
            tabbedPane.removeTabAt(0);
        }

        if (editorData != null) {
            editorData.removeObserver(this);
            editorData.removeObserver(editorActions);
            editorData.removeObserver(editorStatusBar);
            if (!slaveEditor) {
                editorData.fireEvent(EventType.CLOSE);
            }

            editorData.getOptions().save();
        }
        editorData = null;
        editorActions.updateActions();
    }

    public void exit() {
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
    }

    public EditorData getEditorData() {
        return editorData;
    }

    public EditorFile getEditorFile() {
        return editorFile;
    }

    public EditorActions getEditorActions() {
        return editorActions;
    }

    public EditorTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
