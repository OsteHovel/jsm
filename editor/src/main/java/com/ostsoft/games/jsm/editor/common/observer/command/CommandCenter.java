package com.ostsoft.games.jsm.editor.common.observer.command;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;

import java.util.Stack;
import java.util.UUID;


public class CommandCenter implements Observer {

    private final EditorData editorData;
    private final Stack<Command> undoQueue = new Stack<>();
    private final Stack<Command> redoQueue = new Stack<>();

    public CommandCenter(EditorData editorData) {
        this.editorData = editorData;
        editorData.addObserver(this);
    }

    public void executeCommand(Command command) {
        command.execute();
        undoQueue.add(command);
        redoQueue.clear();
        editorData.fireEvent(EventType.UNDO_REDO_CHANGED);
    }

    public void undo() {
        if (!undoQueue.isEmpty()) {
            UUID firstUUID = undoQueue.peek().getUuid();
            String description = "";
            while (true) {
                if (undoQueue.isEmpty()) {
                    break;
                }
                Command lastAction = undoQueue.peek();
                if (lastAction.getUuid() == firstUUID) {
                    undoQueue.pop();
                    lastAction.undo();
                    redoQueue.add(lastAction);
                    description = lastAction.getDescription();
                }
                else {
                    break;
                }
            }
            editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "Undo: " + description);
            editorData.fireEvent(EventType.UNDO_REDO_CHANGED);
        }
    }

    public void redo() {
        if (!redoQueue.isEmpty()) {
            UUID firstUUID = redoQueue.peek().getUuid();
            String description = "";
            while (true) {
                if (redoQueue.isEmpty()) {
                    break;
                }
                Command lastAction = redoQueue.peek();
                if (lastAction.getUuid() == firstUUID) {
                    redoQueue.pop();
                    lastAction.execute();
                    undoQueue.add(lastAction);
                    description = lastAction.getDescription();
                }
                else {
                    break;
                }
            }
            editorData.fireEvent(EventType.STATUS_BAR_MESSAGE, "Redo: " + description);
            editorData.fireEvent(EventType.UNDO_REDO_CHANGED);
        }
    }

    public boolean canUndo() {
        return !undoQueue.isEmpty();
    }

    public boolean canRedo() {
        return !redoQueue.isEmpty();
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        switch (eventType) {
            case CLEAR_UNDO_REDO:
                undoQueue.clear();
                redoQueue.clear();
                editorData.fireEvent(EventType.UNDO_REDO_CHANGED);
                break;
            default:
                break;
        }
    }
}
