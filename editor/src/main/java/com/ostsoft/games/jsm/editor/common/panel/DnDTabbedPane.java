package com.ostsoft.games.jsm.editor.common.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class DnDTabbedPane extends JClosableTabbedPane {
    private static final int SCROLL_SIZE = 20; //Test
    private static final int BUTTON_SIZE = 30; //XXX 30 is magic number of scroll button size
    private static final int LINE_WIDTH = 3;
    public static Rectangle rBackward = new Rectangle();
    public static Rectangle rForward = new Rectangle();
    private final Rectangle lineRect = new Rectangle();
    private final DropMode dropMode = DropMode.INSERT;
    public int dragTabIndex = -1;
    private transient DropLocation dropLocation;

    public DnDTabbedPane() {
        super();
        Handler h = new Handler();
        addMouseListener(h);
        addMouseMotionListener(h);
        addPropertyChangeListener(h);
    }

    private static int getDropLocationIndex(DropLocation loc) {
        return loc == null || !loc.isDropable() ? -1 : loc.getIndex();
    }

    private void clickArrowButton(String actionKey) {
        ActionMap map = getActionMap();
        if (map != null) {
            Action action = map.get(actionKey);
            if (action != null && action.isEnabled()) {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, 0, 0));
            }
        }
    }

    public void autoScrollTest(Point pt) {
        Rectangle r = getTabAreaBounds();
        int tabPlacement = getTabPlacement();
        if (tabPlacement == TOP || tabPlacement == BOTTOM) {
            rBackward.setBounds(r.x, r.y, SCROLL_SIZE, r.height);
            rForward.setBounds(r.x + r.width - SCROLL_SIZE - BUTTON_SIZE, r.y, SCROLL_SIZE + BUTTON_SIZE, r.height);
        }
        else if (tabPlacement == LEFT || tabPlacement == RIGHT) {
            rBackward.setBounds(r.x, r.y, r.width, SCROLL_SIZE);
            rForward.setBounds(r.x, r.y + r.height - SCROLL_SIZE - BUTTON_SIZE, r.width, SCROLL_SIZE + BUTTON_SIZE);
        }
        if (rBackward.contains(pt)) {
            clickArrowButton("scrollTabsBackwardAction");
        }
        else if (rForward.contains(pt)) {
            clickArrowButton("scrollTabsForwardAction");
        }
    }

    public DropLocation dropLocationForPoint(Point p) {
        //boolean isTB = getTabPlacement() == JTabbedPane.TOP || getTabPlacement() == JTabbedPane.BOTTOM;
        switch (dropMode) {
            case INSERT:
                for (int i = 0; i < getTabCount(); i++) {
                    if (getBoundsAt(i).contains(p)) {
                        return new DropLocation(p, i);
                    }
                }
                if (getTabAreaBounds().contains(p)) {
                    return new DropLocation(p, getTabCount());
                }
                break;
            case USE_SELECTION:
            case ON:
            case ON_OR_INSERT:
            default:
                assert false : "Unexpected drop mode";
                break;
        }
        return new DropLocation(p, -1);
    }

    public final DropLocation getDropLocation() {
        return dropLocation;
    }

    public Object setDropLocation(TransferHandler.DropLocation location, Object state, boolean forDrop) {
        DropLocation old = dropLocation;
        if (location == null || !forDrop) {
            dropLocation = new DropLocation(new Point(), -1);
        }
        else if (location instanceof DropLocation) {
            dropLocation = (DropLocation) location;
        }
        firePropertyChange("dropLocation", old, dropLocation);
        return null;
    }

    public void exportTab(int dragIndex, JTabbedPane target, int targetIndex) {
//        System.out.println("exportTab");
        if (targetIndex < 0) {
            return;
        }

        Component cmp = getComponentAt(dragIndex);
        Container parent = target;
        while (parent != null) {
            if (cmp.equals(parent)) { //target == child: JTabbedPane in JTabbedPane
                return;
            }
            parent = parent.getParent();
        }

        Component tab = getTabComponentAt(dragIndex);
        String str = getTitleAt(dragIndex);
        Icon icon = getIconAt(dragIndex);
        String tip = getToolTipTextAt(dragIndex);
        boolean flg = isEnabledAt(dragIndex);
        remove(dragIndex);
        target.insertTab(str, icon, cmp, tip, targetIndex);
        target.setEnabledAt(targetIndex, flg);

        ////ButtonTabComponent
        //if (tab instanceof ButtonTabComponent) {
        //    tab = new ButtonTabComponent(target);
        //}

        target.setTabComponentAt(targetIndex, tab);
        target.setSelectedIndex(targetIndex);
        if (tab instanceof JComponent) {
            ((JComponent) tab).scrollRectToVisible(tab.getBounds());
        }
    }

    public void convertTab(int prev, int next) {
//        System.out.println("convertTab");
        if (next < 0 || prev == next) {
            return;
        }
        Component cmp = getComponentAt(prev);
        Component tab = getTabComponentAt(prev);
        String str = getTitleAt(prev);
        Icon icon = getIconAt(prev);
        String tip = getToolTipTextAt(prev);
        boolean flg = isEnabledAt(prev);
        int tgtindex = prev > next ? next : next - 1;
        remove(prev);
        insertTab(str, icon, cmp, tip, tgtindex);
        setEnabledAt(tgtindex, flg);
        //When you drag'n'drop a disabled tab, it finishes enabled and selected.
        //pointed out by dlorde
        if (flg) {
            setSelectedIndex(tgtindex);
        }
        //I have a component in all tabs (jlabel with an X to close the tab) and when i move a tab the component disappear.
        //pointed out by Daniel Dario Morales Salas
        setTabComponentAt(tgtindex, tab);
    }

    public Rectangle getDropLineRect() {
        DropLocation loc = getDropLocation();
        int index = getDropLocationIndex(loc);
        if (index < 0) {
            lineRect.setRect(0, 0, 0, 0);
            return null;
        }
        boolean isZero = index == 0;
        Rectangle r = getBoundsAt(isZero ? 0 : index - 1);
        if (getTabPlacement() == TOP || getTabPlacement() == BOTTOM) {
            lineRect.setRect(r.x - LINE_WIDTH / 2 + r.width * (isZero ? 0 : 1), r.y, LINE_WIDTH, r.height);
        }
        else {
            lineRect.setRect(r.x, r.y - LINE_WIDTH / 2 + r.height * (isZero ? 0 : 1), r.width, LINE_WIDTH);
        }
        return lineRect;
    }

    public Rectangle getTabAreaBounds() {
        Rectangle tabbedRect = getBounds();
        Component c = getSelectedComponent();
        if (c == null) {
            return tabbedRect;
        }
        int xx = tabbedRect.x;
        int yy = tabbedRect.y;
        Rectangle compRect = getSelectedComponent().getBounds();
        int tabPlacement = getTabPlacement();
        if (tabPlacement == TOP) {
            tabbedRect.height = tabbedRect.height - compRect.height;
        }
        else if (tabPlacement == BOTTOM) {
            tabbedRect.y = tabbedRect.y + compRect.y + compRect.height;
            tabbedRect.height = tabbedRect.height - compRect.height;
        }
        else if (tabPlacement == LEFT) {
            tabbedRect.width = tabbedRect.width - compRect.width;
        }
        else { // if (tabPlacement == RIGHT) {
            tabbedRect.x = tabbedRect.x + compRect.x + compRect.width;
            tabbedRect.width = tabbedRect.width - compRect.width;
        }
        tabbedRect.translate(-xx, -yy);
        //tabbedRect.grow(2, 2);
        return tabbedRect;
    }

    public static final class DropLocation extends TransferHandler.DropLocation {
        private final int index;
        private boolean dropable = true;

        public DropLocation(Point p, int index) {
            super(p);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public boolean isDropable() {
            return dropable;
        }

        public void setDropable(boolean flag) {
            dropable = flag;
        }
//         @Override public String toString() {
//             return getClass().getName()
//                    + "[dropPoint=" + getDropPoint() + ","
//                    + "index=" + index + ","
//                    + "insert=" + isInsert + "]";
//         }
    }

    private class Handler extends MouseAdapter implements PropertyChangeListener { //, BeforeDrag
        private final int gestureMotionThreshold = DragSource.getDragThreshold();
        private Point startPt;
        //private final Integer gestureMotionThreshold = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("DnD.gestureMotionThreshold");

        private void repaintDropLocation() {
            Component c = getRootPane().getGlassPane();
            if (c instanceof GhostGlassPane) {
                GhostGlassPane glassPane = (GhostGlassPane) c;
                glassPane.setTargetTabbedPane(DnDTabbedPane.this);
                glassPane.repaint();
            }
        }

        // PropertyChangeListener
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if ("dropLocation".equals(propertyName)) {
                //System.out.println("propertyChange: dropLocation");
                repaintDropLocation();
            }
        }

        // MouseListener
        @Override
        public void mousePressed(MouseEvent e) {
            DnDTabbedPane src = (DnDTabbedPane) e.getComponent();
            if (src.getTabCount() <= 1) {
                startPt = null;
                return;
            }
            Point tabPt = e.getPoint(); //e.getDragOrigin();
            int idx = src.indexAtLocation(tabPt.x, tabPt.y);
            //disabled tab, null component problem.
            //pointed out by daryl. NullPointerException: i.e. addTab("Tab", null)
            boolean flag = idx < 0 || !src.isEnabledAt(idx) || src.getComponentAt(idx) == null;
            startPt = flag ? null : tabPt;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point tabPt = e.getPoint(); //e.getDragOrigin();
            if (startPt != null && Math.sqrt(Math.pow(tabPt.x - startPt.x, 2) + Math.pow(tabPt.y - startPt.y, 2)) > gestureMotionThreshold) {
                DnDTabbedPane src = (DnDTabbedPane) e.getComponent();
                TransferHandler th = src.getTransferHandler();
                dragTabIndex = src.indexAtLocation(tabPt.x, tabPt.y);
                th.exportAsDrag(src, e, TransferHandler.MOVE);
                lineRect.setRect(0, 0, 0, 0);
                src.getRootPane().getGlassPane().setVisible(true);
                src.setDropLocation(new DropLocation(tabPt, -1), null, true);
                startPt = null;
            }
        }
    }
}
