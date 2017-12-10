package com.ostsoft.games.jsm.editor.credits;

import com.ostsoft.games.jsm.credits.CreditLine;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;

public class CreditsPanelData extends PanelData {
    private CreditLine selectedCreditsLine;

    public CreditsPanelData(EditorData editorData) {
        super(editorData);
        setScale(editorData.getOptions().getCreditsScale());
    }

    public CreditLine getSelectedCreditsLine() {
        return selectedCreditsLine;
    }

    public void setSelectedCreditsLine(CreditLine selectedCreditsLine) {
        this.selectedCreditsLine = selectedCreditsLine;
    }
}
