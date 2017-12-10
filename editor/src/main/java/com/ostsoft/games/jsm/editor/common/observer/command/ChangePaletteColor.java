package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import java.util.UUID;

public class ChangePaletteColor extends Command {

    private final EditorData editorData;
    private final Palette palette;
    private final int index;
    private final DnDColor oldColor;
    private final DnDColor newColor;

    public ChangePaletteColor(EditorData editorData, Palette palette, int index, DnDColor color) {
        this.editorData = editorData;
        this.palette = palette;
        this.index = index;
        this.oldColor = palette.getColors().get(index);
        this.newColor = color;
    }

    public ChangePaletteColor(UUID uuid, EditorData editorData, Palette palette, int index, DnDColor color) {
        super(uuid);
        this.editorData = editorData;
        this.palette = palette;
        this.index = index;
        this.oldColor = palette.getColors().get(index);
        this.newColor = color;
    }


    @Override
    public void execute() {
        palette.getColors().set(index, newColor);
        editorData.fireEvent(EventType.PALETTE_UPDATED);
    }

    @Override
    public void undo() {
        palette.getColors().set(index, oldColor);
        editorData.fireEvent(EventType.PALETTE_UPDATED);
    }

    @Override
    String getDescription() {
        return "Palette color changed";
    }

}
