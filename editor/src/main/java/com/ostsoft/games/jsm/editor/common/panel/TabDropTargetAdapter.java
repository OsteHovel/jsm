package com.ostsoft.games.jsm.editor.common.panel;

import java.awt.*;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

public class TabDropTargetAdapter extends DropTargetAdapter {
    private void clearDropLocationPaint(Component c) {
        System.out.println("------------------- " + c.getName());
        if (c instanceof DnDTabbedPane) {
            DnDTabbedPane t = (DnDTabbedPane) c;
            t.setDropLocation(null, null, false);
            t.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        Component c = dtde.getDropTargetContext().getComponent();
//        System.out.println("DropTargetListener#drop: " + c.getName());
        clearDropLocationPaint(c);
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        Component c = dte.getDropTargetContext().getComponent();
//        System.out.println("DropTargetListener#dragExit: " + c.getName());
        clearDropLocationPaint(c);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        Component c = dtde.getDropTargetContext().getComponent();
//        System.out.println("DropTargetListener#dragEnter: " + c.getName());
    }
//     @Override public void dragOver(DropTargetDragEvent dtde) {
//         //System.out.println("dragOver");
//     }
//     @Override public void dropActionChanged(DropTargetDragEvent dtde) {
//         System.out.println("dropActionChanged");
//     }
}
