package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.util.List;
import java.util.UUID;

public class RemoveFromListCommand<T> extends Command {

    private final EditorData editorData;
    private final List<T> list;
    private final T element;
    private final int oldIndex;
    private final EventType eventType;

    public RemoveFromListCommand(UUID uuid, EditorData editorData, List<T> list, int index, EventType eventType) {
        super(uuid);
        this.editorData = editorData;
        this.list = list;
        this.element = list.get(index);
        this.oldIndex = index;
        this.eventType = eventType;
    }

    @Override
    public void execute() {
        list.remove(oldIndex);
        editorData.fireEvent(eventType);
    }

    @Override
    public void undo() {
        list.add(oldIndex, element);
        editorData.fireEvent(eventType);
    }

    @Override
    String getDescription() {
        return "Removed element at " + oldIndex;
    }

}
