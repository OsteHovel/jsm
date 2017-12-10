package com.ostsoft.games.jsm.util;

public class BtsUtil {
    private BtsUtil() {

    }

    public static boolean isSolid(int bts) {
        int compBts = bts;
        if (isHFlipped(bts)) {
            compBts = bts - 0x40;
        }
        else if (isVFlipped(bts)) {
            compBts = bts - 0x80;
        }
        else if (isHVFlipped(bts)) {
            compBts = bts - 0xC0;
        }

        return !(compBts <= 0x1F);
    }

    public static boolean isHFlipped(int bts) {
        return bts >= 0x40 && bts <= 0x5F;
    }

    public static boolean isVFlipped(int bts) {
        return bts >= 0x80 && bts <= 0x9F;
    }

    public static boolean isHVFlipped(int bts) {
        return bts >= 0xC0 && bts <= 0xDF;
    }
}
