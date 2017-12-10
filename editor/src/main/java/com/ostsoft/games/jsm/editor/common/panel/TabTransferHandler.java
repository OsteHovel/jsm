package com.ostsoft.games.jsm.editor.common.panel;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TabTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor;
    private GhostGlassPane glassPane;
    private DnDTabbedPane source;

    public TabTransferHandler() {
        super();
//        System.out.println("TabTransferHandler");
        localObjectFlavor = new ActivationDataFlavor(EditorTabbedPane.class, DataFlavor.javaJVMLocalObjectMimeType, "EditorTabbedPane");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
//        System.out.println("createTransferable");
        if (c instanceof DnDTabbedPane) {
            source = (DnDTabbedPane) c;
        }
        return new DataHandler(c, localObjectFlavor.getMimeType());
    }

    private boolean isDropable(DnDTabbedPane target, Point pt, int idx) {
        boolean isDropable = false;
        Rectangle tr = target.getTabAreaBounds();
//        if (target.equals(source)) {
//            System.out.println("target == source");
//            isDropable = tr.contains(pt) && idx >= 0 && idx != target.dragTabIndex && idx != target.dragTabIndex + 1;
//        }
        isDropable = tr.contains(pt) && idx >= 0 && idx != target.dragTabIndex && idx != target.dragTabIndex + 1;

//        else {
//            System.out.format("target!=source%n  target: %s%n  source: %s", target.getName(), source.getName());
//            if (source != null && target != source.getComponentAt(source.dragTabIndex)) {
//                isDropable = tr.contains(pt) && idx >= 0;
//            }
//        }
        return isDropable;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        //System.out.println("canImport");
        if (!support.isDrop() || !support.isDataFlavorSupported(localObjectFlavor)) {
//            System.out.println("canImport:" + support.isDrop() + " " + support.isDataFlavorSupported(localObjectFlavor));
            return false;
        }
        support.setDropAction(MOVE);
        DropLocation tdl = support.getDropLocation();
        Point pt = tdl.getDropPoint();
        DnDTabbedPane target = (DnDTabbedPane) support.getComponent();
        target.autoScrollTest(pt);
        DnDTabbedPane.DropLocation dl = target.dropLocationForPoint(pt);
        int idx = dl.getIndex();

        boolean isDropable = isDropable(target, pt, idx);

        glassPane.setVisible(false);
        target.getRootPane().setGlassPane(glassPane);
        // Bug ID: 6700748 Cursor flickering during D&D when using CellRendererPane with validation
        // http://bugs.sun.com/view_bug.do?bug_id=6700748
//        glassPane.setCursor(isDropable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        glassPane.setVisible(true);
//        target.setCursor(isDropable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        //Component c = target.getRootPane().getGlassPane();
        //c.setCursor(isDropable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        if (isDropable) {
            support.setShowDropLocation(true);
            dl.setDropable(true);
            target.setDropLocation(dl, null, true);
            return true;
        }
        else {
            support.setShowDropLocation(false);
            dl.setDropable(false);
            target.setDropLocation(dl, null, false);
            return false;
        }
    }

    //     private static boolean isWebStart() {
//         try {
//             ServiceManager.lookup("javax.jnlp.BasicService");
//             return true;
//         } catch (UnavailableServiceException ex) {
//             return false;
//         }
//     }
    private BufferedImage makeDragTabImage(DnDTabbedPane tabbedPane) {
        Rectangle rect = tabbedPane.getBoundsAt(tabbedPane.dragTabIndex);
        BufferedImage image = new BufferedImage(tabbedPane.getWidth(), tabbedPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        tabbedPane.paint(g);
        g.dispose();
        if (rect.x < 0) {
            rect.translate(-rect.x, 0);
        }
        if (rect.y < 0) {
            rect.translate(0, -rect.y);
        }
        if (rect.x + rect.width > image.getWidth()) {
            rect.width = image.getWidth() - rect.x;
        }
        if (rect.y + rect.height > image.getHeight()) {
            rect.height = image.getHeight() - rect.y;
        }
        return image.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public int getSourceActions(JComponent c) {
//        System.out.println("getSourceActions");
        if (c instanceof DnDTabbedPane) {
            DnDTabbedPane src = (DnDTabbedPane) c;
            if (glassPane == null) {
                glassPane = new GhostGlassPane(src);
                c.getRootPane().setGlassPane(glassPane);
            }
            if (src.dragTabIndex < 0) {
                return NONE;
            }
/*
            glassPane.setImage(makeDragTabImage(src));
/*/
            setDragImage(makeDragTabImage(src)); //java 1.7.0
//*/
            c.getRootPane().getGlassPane().setVisible(true);
            return MOVE;
        }
        return NONE;
    }

    @Override
    public boolean importData(TransferSupport support) {
//        System.out.println("importData");
        if (!canImport(support)) {
            return false;
        }

        DnDTabbedPane target = (DnDTabbedPane) support.getComponent();
        DnDTabbedPane.DropLocation dl = target.getDropLocation();
        try {
            DnDTabbedPane source = (DnDTabbedPane) support.getTransferable().getTransferData(localObjectFlavor);
            int index = dl.getIndex(); //boolean insert = dl.isInsert();
            if (target.equals(source)) {
                source.convertTab(source.dragTabIndex, index); //getTargetTabIndex(e.getLocation()));
            }
            else {
                source.exportTab(source.dragTabIndex, target, index);
            }
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
//        System.out.println("exportDone");
        DnDTabbedPane src = (DnDTabbedPane) c;
        src.setDropLocation(null, null, false);
        src.repaint();
        glassPane.setVisible(false);
        src.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        //glassPane = null;
        //source = null;
    }
}
