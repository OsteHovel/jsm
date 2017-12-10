package com.ostsoft.games.jsm.editor.options;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class OptionsFrame extends JFrame {
    private final EditorData editorData;
    private final List<OptionsPane> optionsPaneList = new ArrayList<>();
    private final Action okAction = new OKAction();
    private final Action cancelAction = new CancelAction();

    public OptionsFrame(EditorData editorData) {
        this.editorData = editorData;
        if (editorData == null) {
            return;
        }

        setVisible(true);
        setTitle("jSM - Options");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        Box horizontalBox = Box.createHorizontalBox();
        getContentPane().add(horizontalBox, BorderLayout.SOUTH);

        JButton btnOk = new JButton(okAction);
        horizontalBox.add(btnOk);

        JButton btnCancel = new JButton(cancelAction);
        horizontalBox.add(btnCancel);

        getRootPane().setDefaultButton(btnOk);
        setSize(500, 460);

        optionsPaneList.add(new GeneralPane());
        optionsPaneList.add(new FormatPane());
        optionsPaneList.add(new AnimationPane());
        optionsPaneList.add(new CreditsPane());

        for (OptionsPane optionsPane : optionsPaneList) {
            optionsPane.load(editorData.getOptions());
            tabbedPane.addTab(optionsPane.getName(), null, new JScrollPane(optionsPane), null);
        }

        getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
        getRootPane().getActionMap().put("cancelAction", cancelAction);
    }

    private class OKAction extends AbstractAction {
        public OKAction() {
            putValue(NAME, "OK");
            putValue(SHORT_DESCRIPTION, "Save settings");
        }

        public void actionPerformed(ActionEvent e) {
            for (OptionsPane optionsPane : optionsPaneList) {
                optionsPane.save(editorData.getOptions());
            }
            if (editorData != null) {
                editorData.fireEvent(EventType.OPTION_UPDATED);
            }
            dispatchEvent(new WindowEvent(OptionsFrame.this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private class CancelAction extends AbstractAction {
        public CancelAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(NAME, "Cancel");
            putValue(SHORT_DESCRIPTION, "Do not save settings");
        }

        public void actionPerformed(ActionEvent e) {
            dispatchEvent(new WindowEvent(OptionsFrame.this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
