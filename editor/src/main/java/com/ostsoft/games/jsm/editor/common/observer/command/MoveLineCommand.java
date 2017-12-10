package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.list.EventListModel;
import com.ostsoft.games.jsm.editor.common.observer.EventType;

import java.util.List;
import java.util.UUID;

public class MoveLineCommand<T> extends Command {

    private final EditorData editorData;
    private final EventListModel model;
    private final List<T> list;
    private final T element;
    private final int newIndex;
    private final int oldIndex;
    private final EventType eventType;

    public MoveLineCommand(UUID uuid, EditorData editorData, EventListModel model, List<T> list, T element, int newIndex, EventType eventType) {
        super(uuid);
        this.editorData = editorData;
        this.model = model;
        this.list = list;
        this.element = element;
        this.oldIndex = list.indexOf(element);
        this.newIndex = newIndex + (oldIndex < newIndex ? -1 : 0);
        this.eventType = eventType;
    }

    @Override
    public void execute() {
        list.remove(oldIndex);
        model.fireIntervalRemoved(this, oldIndex, oldIndex);
        list.add(newIndex, element);
        model.fireIntervalAdded(this, newIndex, newIndex);
        editorData.fireEvent(eventType);
    }

    @Override
    public void undo() {
        list.remove(newIndex);
        model.fireIntervalRemoved(this, newIndex, newIndex);
        list.add(oldIndex, element);
        model.fireIntervalAdded(this, oldIndex, oldIndex);
        editorData.fireEvent(eventType);
    }

    @Override
    String getDescription() {
        return "Move element from " + oldIndex + " to " + newIndex + ")";
    }

}
