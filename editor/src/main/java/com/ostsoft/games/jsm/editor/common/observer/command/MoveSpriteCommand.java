package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

public class MoveSpriteCommand extends Command {

    private final EditorData editorData;
    private final SpriteMapEntry spriteMapEntry;
    private final int newX, newY;
    private final int oldX, oldY;

    public MoveSpriteCommand(EditorData editorData, SpriteMapEntry spriteMapEntry, int newX, int newY) {
        this.editorData = editorData;
        this.spriteMapEntry = spriteMapEntry;
        this.newX = newX;
        this.newY = newY;
        this.oldX = spriteMapEntry.getX();
        this.oldY = spriteMapEntry.getY();
    }

    @Override
    public void execute() {
        spriteMapEntry.setX(newX);
        spriteMapEntry.setY(newY);
        editorData.fireEvent(EventType.SPRITE_MOVED);
    }

    @Override
    public void undo() {
        spriteMapEntry.setX(oldX);
        spriteMapEntry.setY(oldY);
        editorData.fireEvent(EventType.SPRITE_MOVED);
    }

    @Override
    String getDescription() {
        return "Move sprite from (" + oldX + ", " + oldY + ") to (" + newX + ", " + newY + ")";
    }

}
