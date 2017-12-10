package com.ostsoft.games.jsm.editor.common.list;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.command.AddToListCommand;
import com.ostsoft.games.jsm.editor.common.observer.command.Command;
import com.ostsoft.games.jsm.editor.common.observer.command.MoveLineCommand;
import com.ostsoft.games.jsm.editor.common.observer.command.RemoveFromListCommand;

import java.util.List;
import java.util.UUID;

public class EventListModel<T> extends ListModel<T> {

    private final EditorData editorData;
    private final EventType updateEvent;

    public EventListModel(List<T> list, EditorData editorData, EventType updateEvent) {
        super(list);
        this.editorData = editorData;
        this.updateEvent = updateEvent;
    }

    public void add(UUID uuid, int index, T value) {
        AddToListCommand<T> command = new AddToListCommand<T>(uuid, editorData, list, value, index, updateEvent);
        editorData.getCommandCenter().executeCommand(command);
        fireIntervalAdded(this, index, index);
    }

    public void remove(UUID uuid, int index) {
        RemoveFromListCommand<T> command = new RemoveFromListCommand<T>(uuid, editorData, list, index, updateEvent);
        editorData.getCommandCenter().executeCommand(command);
        fireIntervalRemoved(this, index, index);
    }


    public void move(UUID uuid, int newIndex, T value) {
        int oldIndex = list.indexOf(value);
        Command command = new MoveLineCommand<T>(uuid, editorData, this, list, value, newIndex, updateEvent);
        editorData.getCommandCenter().executeCommand(command);
        fireIntervalRemoved(this, oldIndex, oldIndex);
        fireIntervalAdded(this, newIndex, newIndex);
    }

    public void moveSelection(UUID uuid, int newIndex, T[] values) {
        if (values.length <= 0) {
            return;
        }

        for (int i = 0; i < values.length; i++) {
            T value = values[i];
            if (list.indexOf(value) > newIndex) {
                continue;
            }
            MoveLineCommand<T> command = new MoveLineCommand<>(uuid, editorData, this, list, value, newIndex, updateEvent);
            editorData.getCommandCenter().executeCommand(command);
        }

        for (int i = values.length - 1; i >= 0; i--) {
            T value = values[i];
            if (list.indexOf(value) <= newIndex) {
                continue;
            }
            MoveLineCommand<T> command = new MoveLineCommand<>(uuid, editorData, this, list, value, newIndex, updateEvent);
            editorData.getCommandCenter().executeCommand(command);
        }

    }

    @Override
    public void fireIntervalAdded(Object source, int index0, int index1) {
        super.fireIntervalAdded(source, index0, index1);
    }

    @Override
    public void fireIntervalRemoved(Object source, int index0, int index1) {
        super.fireIntervalRemoved(source, index0, index1);
    }
}
