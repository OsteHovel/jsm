package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.util.UUID;

public class ChangeTileBytes extends Command {

    private final EditorData editorData;
    private final Tile tile;
    private final byte[] oldBytes;
    private final byte[] newBytes;

    public ChangeTileBytes(EditorData editorData, Tile tile, byte[] newBytes) {
        this.editorData = editorData;
        this.tile = tile;
        this.oldBytes = tile.getBytes();
        this.newBytes = newBytes;
    }

    public ChangeTileBytes(UUID uuid, EditorData editorData, Tile tile, byte[] newBytes) {
        super(uuid);
        this.editorData = editorData;
        this.tile = tile;
        this.oldBytes = tile.getBytes();
        this.newBytes = newBytes;
    }


    @Override
    public void execute() {
        tile.setBytes(newBytes);
        editorData.fireEvent(EventType.TILE_UPDATED);
    }

    @Override
    public void undo() {
        tile.setBytes(oldBytes);
        editorData.fireEvent(EventType.TILE_UPDATED);
    }

    @Override
    String getDescription() {
        return "Updated tile bytes";
    }

}
