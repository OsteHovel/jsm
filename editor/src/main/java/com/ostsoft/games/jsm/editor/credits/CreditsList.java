package com.ostsoft.games.jsm.editor.credits;

import com.ostsoft.games.jsm.editor.common.list.CustomListRenderer;
import com.ostsoft.games.jsm.editor.common.list.EventListItemTransferHandler;

import javax.swing.*;
import java.awt.*;

public class CreditsList<T> extends JList<T> {
    public CreditsList(CreditsPanelData creditsPanelData) {
        super();

        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setTransferHandler(new EventListItemTransferHandler<T>(TransferHandler.COPY_OR_MOVE));
        setDropMode(DropMode.INSERT);
        setDragEnabled(true);
        setLayoutOrientation(JList.VERTICAL);
        setCellRenderer(new CustomListRenderer<>(creditsPanelData, true, false));
        setSelectionForeground(new Color(getSelectionBackground().getRed(), getSelectionBackground().getGreen(), getSelectionBackground().getBlue(), 128));
    }
}


