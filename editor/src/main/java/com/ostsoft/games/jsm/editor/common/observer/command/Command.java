package com.ostsoft.games.jsm.editor.common.observer.command;

import java.util.UUID;

public abstract class Command {

    private final UUID uuid;

    protected Command(UUID uuid) {
        this.uuid = uuid;
    }

    public Command() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    abstract void execute();

    abstract void undo();

    abstract String getDescription();

}
