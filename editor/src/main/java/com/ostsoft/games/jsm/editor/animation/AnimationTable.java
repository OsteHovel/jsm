package com.ostsoft.games.jsm.editor.animation;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.samus.SamusAnimation;
import com.ostsoft.games.jsm.animation.samus.SamusFrame;
import com.ostsoft.games.jsm.animation.tile.TileFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.table.EditorTable;
import com.ostsoft.games.jsm.editor.common.table.columns.BooleanMethodRow;
import com.ostsoft.games.jsm.editor.common.table.columns.EnumRow;
import com.ostsoft.games.jsm.editor.common.table.columns.IntegerMethodRow;
import com.ostsoft.games.jsm.editor.common.table.columns.IntegerRow;
import com.ostsoft.games.jsm.editor.common.table.columns.PointerRow;
import com.ostsoft.games.jsm.editor.common.table.columns.TileRow;

public class AnimationTable extends EditorTable implements Observer {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;

    public AnimationTable(final EditorData editorData, final AnimationPanelData animationPanelData) {
        super(editorData);
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (cellEditor != null) {
            cellEditor.cancelCellEditing();
        }

        if (eventType == EventType.SPRITE_SELECTED && animationPanelData.getSelectEntry() != null) {
            model.clear();
//            model.add(new PointerRow(editorData, new String[]{"Offset"}, animationPanelData.getSelectEntry(), "getOffset", "setOffset", EventType.SPRITE_UPDATED));
            model.add(new IntegerMethodRow(editorData, new String[]{"X"}, animationPanelData.getSelectEntry(), "getX", "setX", EventType.SPRITE_UPDATED));
            model.add(new IntegerMethodRow(editorData, new String[]{"Y"}, animationPanelData.getSelectEntry(), "getY", "setY", EventType.SPRITE_UPDATED));
            model.add(new IntegerMethodRow(editorData, new String[]{"TileIndex"}, animationPanelData.getSelectEntry(), "getTileIndex", "setTileIndex", EventType.SPRITE_UPDATED));
            model.add(new TileRow(editorData, new String[]{"Select tile"}, animationPanelData.getSelectEntry(), animationPanelData, "getTileIndex", "setTileIndex", EventType.SPRITE_UPDATED));
            model.add(new BooleanMethodRow(editorData, new String[]{"16x16"}, animationPanelData.getSelectEntry(), "is16x16tile", "set16x16tile", EventType.SPRITE_UPDATED));
            model.add(new BooleanMethodRow(editorData, new String[]{"HFlip"}, animationPanelData.getSelectEntry(), "isHFlip", "setHFlip", EventType.SPRITE_UPDATED));
            model.add(new BooleanMethodRow(editorData, new String[]{"VFlip"}, animationPanelData.getSelectEntry(), "isVFlip", "setVFlip", EventType.SPRITE_UPDATED));
            model.add(new BooleanMethodRow(editorData, new String[]{"xOffsetHigh (auto)"}, animationPanelData.getSelectEntry(), "isXOffsetHigh", "setXOffsetHigh", EventType.SPRITE_UPDATED));
            model.add(new BooleanMethodRow(editorData, new String[]{"isLoadNextGraphicsPage (auto)"}, animationPanelData.getSelectEntry(), "isLoadNextGraphicsPage", "setLoadNextGraphicsPage", EventType.SPRITE_UPDATED));
        }
        else if (eventType == EventType.ANIMATION && animationPanelData.getAnimation() != null) {
            model.clear();
            if (animationPanelData.getAnimation() instanceof SamusAnimation) {
                model.add(new PointerRow(editorData, new String[]{"FrameSpeedPointer (read-only)"}, animationPanelData.getAnimation(), "getFrameSpeedPointer", null, EventType.ANIMATION_UPDATED));
                model.add(new EnumRow(editorData, new String[]{"Terminator (read-only)"}, animationPanelData.getAnimation(), "animationTerminator", EventType.ANIMATION_UPDATED));
            }
        }
        else if (eventType == EventType.ANIMATION_FRAME && animationPanelData.getAnimationFrame() != null) {
            AnimationFrame animationFrame = animationPanelData.getAnimationFrame();
            model.clear();
            model.add(new IntegerMethodRow(editorData, new String[]{"Number of frames"}, animationFrame, "getFrameLength", "setFrameLength", EventType.ANIMATION_FRAME_UPDATED));
            if (animationFrame instanceof SamusFrame) {
                SamusFrame samusFrame = (SamusFrame) animationFrame;
                model.add(new IntegerRow(editorData, new String[]{"TopTable"}, samusFrame.dmaFrame, "indexTopHalfTable", EventType.ANIMATION_FRAME_UPDATED));
                model.add(new IntegerRow(editorData, new String[]{"TopEntry"}, samusFrame.dmaFrame, "indexTopHalfEntry", EventType.ANIMATION_FRAME_UPDATED));
                model.add(new IntegerRow(editorData, new String[]{"BottomTable"}, samusFrame.dmaFrame, "indexBottomHalfTable", EventType.ANIMATION_FRAME_UPDATED));
                model.add(new IntegerRow(editorData, new String[]{"BottomEntry"}, samusFrame.dmaFrame, "indexBottomHalfEntry", EventType.ANIMATION_FRAME_UPDATED));
            }
            else if (animationFrame instanceof TileFrame) {
//                model.add(new IntegerRow(editorData, new String[]{"GFXPointer"}, animationFrame, "gfxPointer", EventType.ANIMATION_FRAME_UPDATED));
//                model.add(new StaticStringRow(editorData, new String[]{"GFXPointer(hex)"}, "0x" + Integer.toHexString(((TileFrame) animationFrame).gfxPointer)));
            }
        }

        model.update();
    }
}
