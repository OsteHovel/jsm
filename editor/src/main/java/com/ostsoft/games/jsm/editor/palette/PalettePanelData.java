package com.ostsoft.games.jsm.editor.palette;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PaletteData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import java.awt.*;

public class PalettePanelData extends PanelData implements PaletteData {
    private Palette selectedPalette;
    private DnDColor selectedColor;

    public PalettePanelData(EditorData editorData) {
        super(editorData);
    }

    @Override
    public Palette getPalette() {
        return selectedPalette;
    }

    @Override
    public void setPalette(Palette palette) {
        this.selectedPalette = palette;
    }

    public DnDColor getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(DnDColor selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = new DnDColor(selectedColor);
    }
}
