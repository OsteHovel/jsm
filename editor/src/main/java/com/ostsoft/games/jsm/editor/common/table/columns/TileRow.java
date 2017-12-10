package com.ostsoft.games.jsm.editor.common.table.columns;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

public class TileRow extends IntegerMethodRow {
    private final AnimationPanelData animationPanelData;

    public TileRow(EditorData table, Object[] columns, Object o, AnimationPanelData animationPanelData, String getMethodName, String setMethodName, EventType eventType) {
        super(table, columns, o, getMethodName, setMethodName, eventType);
        this.animationPanelData = animationPanelData;
    }

    public AnimationPanelData getAnimationPanelData() {
        return animationPanelData;
    }
}
