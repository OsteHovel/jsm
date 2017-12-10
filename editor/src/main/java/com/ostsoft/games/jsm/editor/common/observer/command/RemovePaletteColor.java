package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import java.util.UUID;

public class RemovePaletteColor extends Command {

    private final EditorData editorData;
    private final Palette palette;
    private final int index;
    private DnDColor color;

    public RemovePaletteColor(EditorData editorData, Palette palette, int index) {
        this.editorData = editorData;
        this.palette = palette;
        this.index = index;
    }

    public RemovePaletteColor(UUID uuid, EditorData editorData, Palette palette, int index) {
        super(uuid);
        this.editorData = editorData;
        this.palette = palette;
        this.index = index;
    }


    @Override
    public void execute() {
        color = palette.getColors().remove(index);
        editorData.fireEvent(EventType.PALETTE_UPDATED);
    }

    @Override
    public void undo() {
        palette.getColors().add(index, color);
        editorData.fireEvent(EventType.PALETTE_UPDATED);
    }

    @Override
    String getDescription() {
        return "Palette color removed";
    }

}
