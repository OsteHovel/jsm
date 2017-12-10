package com.ostsoft.games.jsm.palette;


import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import java.util.List;

public class Palette {
    private final boolean compressed;
    private String name;
    private int offset;
    private int length;
    private List<DnDColor> colors;

    public Palette(String name, int offset, boolean compressed, int length) {
        this.name = name;
        this.offset = offset;
        this.compressed = compressed;
        this.length = length;
    }

    public void load(ByteStream stream) {
        byte[] palette;
        if (compressed) {
            palette = CompressUtil.decompress(stream, BitHelper.snesToPc(offset));
            this.length = palette.length;
        }
        else {
            stream.setPosition(BitHelper.snesToPc(offset));
            palette = new byte[length];
            stream.read(palette, 0, length);
        }

        colors = PaletteUtil.decodePaletteToColors(palette);
    }

    public void save(ByteStream stream) {
        if (offset == 0 || length == 0 || compressed) {
            return;
        }
        stream.setPosition(BitHelper.snesToPc(offset));
        stream.writeBytes(PaletteUtil.encodeColorsToPalette(colors), length);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isCompressed() {
        return compressed;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<DnDColor> getColors() {
        return colors;
    }
}
