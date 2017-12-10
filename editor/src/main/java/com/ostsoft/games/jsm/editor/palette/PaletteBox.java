package com.ostsoft.games.jsm.editor.palette;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.list.ComboListModel;
import com.ostsoft.games.jsm.palette.Palette;

import javax.swing.*;

public class PaletteBox extends JComboBox<Palette> {
    public PaletteBox(EditorData editorData) {
        super(new ComboListModel<>(editorData.getSuperMetroid().getPaletteManager().getPalettes()));
    }
}
