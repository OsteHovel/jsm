package com.ostsoft.games.jsm.palette;

import com.ostsoft.games.jsm.util.BitHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaletteUtil {
    public static List<DnDColor> decodePaletteToColors(byte[] palette) {
        int numberOfColors = (palette.length) / 2;

        java.util.List<DnDColor> colors = new ArrayList<>(numberOfColors);
//        colors.add(new Color(0, 0, 0, 0));
        for (int i = 0; i < numberOfColors; i++) {
//            if ((i % 16) == 0) {
//                colors.add(new Color(0, 0, 0, 0));
//                continue;
//            }
            int colorAs15bit = (Byte.toUnsignedInt(palette[(i * 2) + 1]) << 8) | Byte.toUnsignedInt(palette[i * 2]);
            colors.add(new DnDColor(getRed(colorAs15bit), getGreen(colorAs15bit), getBlue(colorAs15bit)));
        }
        return colors;
    }

    public static byte[] encodeColorsToPalette(List<DnDColor> colors) {
        byte[] palette = new byte[colors.size() * 2];
        for (int i = 0; i < colors.size(); i++) {
            int color15bpp = getColor15bpp(colors.get(i).getRed(), colors.get(i).getGreen(), colors.get(i).getBlue());
            palette[(i * 2) + 1] = BitHelper.unsignedIntToSignedByte(((color15bpp >> 8) & 0xFF));
            palette[(i * 2)] = BitHelper.unsignedIntToSignedByte(color15bpp & 0xFF);
        }
        return palette;
    }

    public static int getColor15bpp(int red, int green, int blue) {
        return ((red >> 3) & 31) | ((green & 0xF8) << 2) | ((blue & 0xF8) << 7);
    }

    public static int getRed(int color15bpp) {
        return (color15bpp & 31) << 3;
    }

    public static int getGreen(int color15bpp) {
        return (color15bpp >> 2) & 0xF8;
    }

    public static int getBlue(int color15bpp) {
        return (color15bpp >> 7) & 0xF8;
    }

    public static byte[] getTPLCompatiblePalette(Palette palette) {
        byte[] bytes = new byte[(palette.getColors().size() * 3) + 4];
        bytes[0] = 'T';
        bytes[1] = 'P';
        bytes[2] = 'L';
        bytes[3] = 0x0;
        int pos = 4;
        for (Color color : palette.getColors()) {
            bytes[pos] = (byte) color.getRed();
            bytes[pos + 1] = (byte) color.getGreen();
            bytes[pos + 2] = (byte) color.getBlue();
            pos += 3;
        }
        return bytes;
    }

    public static List<DnDColor> parseTPLCompatiblePalette(byte[] palette) {
        int numberOfColors = (palette.length - 4) / 3;
        java.util.List<DnDColor> colors = new ArrayList<>(numberOfColors);
        for (int i = 0; i < numberOfColors; i++) {
            colors.add(new DnDColor(Byte.toUnsignedInt(palette[i * 3 + 4]), Byte.toUnsignedInt(palette[i * 3 + 5]), Byte.toUnsignedInt(palette[i * 3 + 6])));
        }
        return colors;
    }
}
