package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.util.List;
import java.util.UUID;

public class AddToListCommand<T> extends Command {

    private final EditorData editorData;
    private final List<T> list;
    private final T element;
    private final int newIndex;
    private final EventType eventType;

    public AddToListCommand(UUID uuid, EditorData editorData, List<T> list, T element, int newIndex, EventType eventType) {
        super(uuid);
        this.editorData = editorData;
        this.list = list;
        this.element = element;
        this.newIndex = newIndex;
        this.eventType = eventType;
    }

    @Override
    public void execute() {
        list.add(newIndex, element);
        editorData.fireEvent(eventType);
    }

    @Override
    public void undo() {
        list.remove(newIndex);
        editorData.fireEvent(eventType);
    }

    @Override
    String getDescription() {
        return "Added element at " + newIndex;
    }

}
