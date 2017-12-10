package com.ostsoft.games.jsm.editor.credits;

import com.ostsoft.games.jsm.credits.BlankLine;
import com.ostsoft.games.jsm.credits.CreditLine;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.table.EditorTable;
import com.ostsoft.games.jsm.editor.common.table.columns.IntegerRow;

public class CreditsTable extends EditorTable implements Observer {
    private final EditorData editorData;
    private final CreditsPanelData creditsPanelData;

    public CreditsTable(final EditorData editorData, final CreditsPanelData creditsPanelData) {
        super(editorData);
        this.editorData = editorData;
        this.creditsPanelData = creditsPanelData;
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (cellEditor != null) {
            cellEditor.cancelCellEditing();
        }

        if (eventType == EventType.CREDITSLINE_SELECTED) {
            if (creditsPanelData.getSelectedCreditsLine() != null) {
                CreditLine creditsLine = creditsPanelData.getSelectedCreditsLine();
                if (creditsLine instanceof BlankLine) {
                    model.clear();
                    model.add(new IntegerRow(editorData, new String[]{"Number of lines"}, creditsPanelData.getSelectedCreditsLine(), "numberOfLines", EventType.CREDITSLINE_UPDATED));
                }
                else {
                    model.clear();
                }
            }
            else {
                model.clear();
            }
        }

        model.update();
    }
}
