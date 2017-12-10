package com.ostsoft.games.jsm.editor;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.editor.common.gui.EditorActions;
import com.ostsoft.games.jsm.editor.common.observer.Observable;
import com.ostsoft.games.jsm.editor.common.observer.command.CommandCenter;
import com.ostsoft.games.jsm.editor.options.Options;

public class EditorData extends Observable {
    private final CommandCenter commandCenter = new CommandCenter(this);
    private final SuperMetroid superMetroid;
    private final EditorActions editorActions;
    private final Options options = new Options();

    public EditorData(EditorActions editorActions, SuperMetroid superMetroid) {
        this.editorActions = editorActions;
        this.superMetroid = superMetroid;
        options.load();
    }

    public CommandCenter getCommandCenter() {
        return commandCenter;
    }

    public SuperMetroid getSuperMetroid() {
        return superMetroid;
    }

    public Options getOptions() {
        return options;
    }

    public EditorActions getEditorActions() {
        return editorActions;
    }
}
