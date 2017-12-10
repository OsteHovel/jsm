package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import java.util.UUID;

public class AddPaletteColor extends Command {

    private final EditorData editorData;
    private final Palette palette;
    private final DnDColor newColor;
    private int index;

    public AddPaletteColor(EditorData editorData, Palette palette, DnDColor color) {
        this.editorData = editorData;
        this.palette = palette;
        this.newColor = color;
    }

    public AddPaletteColor(UUID uuid, EditorData editorData, Palette palette, DnDColor color) {
        super(uuid);
        this.editorData = editorData;
        this.palette = palette;
        this.newColor = color;
    }


    @Override
    public void execute() {
        this.index = palette.getColors().size();
        palette.getColors().add(newColor);
        editorData.fireEvent(EventType.PALETTE_UPDATED);
    }

    @Override
    public void undo() {
        palette.getColors().remove(index);
        editorData.fireEvent(EventType.PALETTE_UPDATED);
    }

    @Override
    String getDescription() {
        return "Added palette color";
    }

}
