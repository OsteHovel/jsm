package com.ostsoft.games.jsm.editor.options;

import javax.swing.*;

public abstract class OptionsPane extends JPanel {
    public OptionsPane(String name) {
        setName(name);
    }

    abstract void load(Options options);

    abstract void save(Options options);
}
