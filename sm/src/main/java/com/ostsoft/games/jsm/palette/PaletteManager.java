package com.ostsoft.games.jsm.palette;

import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class PaletteManager {
    private List<Palette> palettes = new ArrayList<>();

    public PaletteManager(ByteStream stream) {
        for (PaletteEnum paletteEnum : PaletteEnum.values()) {
            int address = paletteEnum.address;
            if (!paletteEnum.direct) {
                stream.setPosition(BitHelper.snesToPc(address));
                address = stream.readReversedUnsigned3Bytes();
            }
            Palette e = new Palette(paletteEnum.name().replace("_", " "), address, paletteEnum.compressed, paletteEnum.length);
            e.load(stream);
            palettes.add(e);
        }
    }

    public void save(ByteStream stream) {
        for (Palette palette : palettes) {
            palette.save(stream);
        }
    }

    public Palette getPalette(String paletteName) {
        for (Palette palette : palettes) {
            if (palette.getName().equals(paletteName)) {
                return palette;
            }
        }
        return null;
    }

    public Palette getPalette(PaletteEnum paletteEnum) {
        return getPalette(paletteEnum.name().replace("_", " "));
    }

    public List<Palette> getPalettes() {
        return palettes;
    }

    public void addPalette(Palette palette) {
        palettes.add(palette);
    }
}