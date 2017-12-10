package com.ostsoft.games.jsm.editor.common;

import com.ostsoft.games.jsm.editor.EditorData;

public class PanelData {
    private float scale;

    public PanelData(EditorData editorData) {
        scale = editorData.getOptions().getDefaultScale();
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        if (scale > 0) {
            this.scale = scale;
        }
    }
}
