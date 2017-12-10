package com.ostsoft.games.jsm.editor.common.panel;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

public abstract class EditorPanel extends JPanel {
    protected final EditorData editorData;
    protected final PanelData panelData;
    protected final MouseWheelListener zoomWheelListener;
    protected final List<Component> mnTools = new ArrayList<>();

    protected EditorPanel(EditorData editorData, PanelData panelData) {
        this.editorData = editorData;
        this.panelData = panelData;

        zoomWheelListener = e -> {
            if (e.isControlDown()) {
                panelData.setScale((float) (panelData.getScale() - (e.getPreciseWheelRotation() * editorData.getOptions().getZoomWheelStep())));
                handlePanelEvent(PanelEvent.SCALE);
                e.consume();
            }
        };
        addMouseWheelListener(zoomWheelListener);
    }

    public void handlePanelEvent(PanelEvent panelEvent) {
        switch (panelEvent) {
            case ZOOM_IN:
                panelData.setScale(panelData.getScale() + editorData.getOptions().getZoomStep());
                handlePanelEvent(PanelEvent.SCALE);
                break;
            case ZOOM_OUT:
                panelData.setScale(panelData.getScale() - editorData.getOptions().getZoomStep());
                handlePanelEvent(PanelEvent.SCALE);
                break;
        }
    }

    public List<Component> getToolsMenu() {
        return mnTools;
    }
}