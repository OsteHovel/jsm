package com.ostsoft.games.jsm.editor.common.panel;

import javax.swing.*;
import java.awt.*;
import java.util.TooManyListenersException;

public class EditorTabbedPane extends DnDTabbedPane {
    public EditorTabbedPane() {
        super();

        TabTransferHandler handler = new TabTransferHandler();
        try {
            setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            setTransferHandler(handler);
            getDropTarget().addDropTargetListener(new TabDropTargetAdapter());
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        setTransferHandler(handler);
    }

    @Override
    public void removeTabAt(int index) {
        Component component = getComponentAt(index);
        if (component instanceof EditorPanel) {
            ((EditorPanel) component).handlePanelEvent(PanelEvent.CLOSEPANEL);
        }

        super.removeTabAt(index);
    }
}


