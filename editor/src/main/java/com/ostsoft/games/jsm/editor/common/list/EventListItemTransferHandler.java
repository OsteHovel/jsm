package com.ostsoft.games.jsm.editor.common.list;

import com.ostsoft.games.jsm.common.DnD;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

//@camickr already suggested above.
//http://docs.oracle.com/javase/tutorial/uiswing/dnd/dropmodedemo.html
public class EventListItemTransferHandler<T> extends TransferHandler {
    private final int sourceAction;
    private final DataFlavor localObjectFlavor;

    private UUID uuid;
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.

    public EventListItemTransferHandler(int sourceAction) {
        this.sourceAction = sourceAction;
        localObjectFlavor = new ActivationDataFlavor(
                Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
    }


    @Override
    protected Transferable createTransferable(JComponent c) {
        JList list = (JList) c;
        indices = list.getSelectedIndices();
        uuid = UUID.randomUUID();
        List selectedValuesList = list.getSelectedValuesList();
        Object[] transferredObjects = selectedValuesList.toArray(new Object[selectedValuesList.size()]);
        return new DataHandler(transferredObjects, localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferSupport info) {
        return info.isDataFlavorSupported(localObjectFlavor);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return sourceAction; //TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }

        JList<T> target = (JList<T>) info.getComponent();
        if (target.getModel() instanceof EventListModel) {
            EventListModel<T> listModel = (EventListModel<T>) target.getModel();

            int index;
            if (info.isDrop()) {
                JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
                index = dl.getIndex();
            }
            else {
                index = target.getSelectedIndex();
            }

            int max = listModel.getSize();
            if (index < 0 || index > max) {
                index = max;
            }

            addIndex = index;
            try {
                Object[] values = (Object[]) info.getTransferable().getTransferData(
                        localObjectFlavor);
                addCount = values.length;
                for (int i = 0; i < values.length; i++) {
                    int idx = index++;
                    addObject(info, listModel, values, i, idx);
                    target.addSelectionInterval(idx, idx);
                }
                return true;
            } catch (UnsupportedFlavorException | IOException ufe) {
                ufe.printStackTrace();
            }
        }
        return false;
    }

    protected void addObject(TransferSupport info, EventListModel<T> listModel, Object[] values, int i, int idx) {
        if ((info.isDrop() && info.getDropAction() == COPY) || !info.isDrop()) {
            if (values[i] instanceof DnD) {
                addToList(listModel, ((DnD) values[i]).clone(), idx);
            }
            else {
                System.out.println("[EventListItemTransferHandler] Item was MOVED even when it should have been COPIED");
                addToList(listModel, values[i], idx);
            }
        }
        else {
            addToList(listModel, values[i], idx);
        }
    }

    protected void addToList(EventListModel<T> listModel, Object value, int idx) {
        listModel.add(uuid, idx, (T) value);
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }

    private void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            JList source = (JList) c;
            if (source.getModel() instanceof EventListModel) {
                EventListModel model = (EventListModel) source.getModel();
                if (addCount > 0) {
                    //http://java-swing-tips.googlecode.com/svn/trunk/DnDReorderList/src/java/example/MainPanel.java
                    for (int i = 0; i < indices.length; i++) {
                        if (indices[i] >= addIndex) {
                            indices[i] += addCount;
                        }
                    }
                }
                for (int i = indices.length - 1; i >= 0; i--) {
                    model.remove(uuid, indices[i]);
                }
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }

}
