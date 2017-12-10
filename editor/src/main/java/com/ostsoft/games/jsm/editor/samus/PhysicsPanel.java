package com.ostsoft.games.jsm.editor.samus;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;

import javax.swing.*;
import java.awt.*;

public class PhysicsPanel extends EditorPanel {

    private final VariableTable table;

    public PhysicsPanel(EditorData editorData) {
        super(editorData, new PanelData(editorData));
        setLayout(new BorderLayout(0, 0));

        table = new VariableTable(editorData, editorData.getSuperMetroid().getPhysics().getVariables());
        add(new JScrollPane(table), BorderLayout.CENTER);
        editorData.addObserver(table);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        super.handlePanelEvent(panelEvent);
        if (panelEvent == PanelEvent.CLOSEPANEL) {
            editorData.removeObserver(table);
        }
    }
}
