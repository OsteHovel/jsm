package com.ostsoft.games.jsm.palette;

public enum PaletteEnum {
    // Samus
    SAMUS_POWERSUIT(0x9B9400, true, false, 32),
    SAMUS_POWERSUIT_SCREWATTACK(0x9B9CA0, true, false, 160),
    SAMUS_VARIASUIT(0x9B9520, true, false, 32),
    SAMUS_VARIASUIT_SCREWATTACK(0x9B9EA0, true, false, 160),
    SAMUS_GRAVITYSUIT(0x9B9800, true, false, 32),
    SAMUS_GRAVITYSUIT_SCREWATTACK(0x9BA0A0, true, false, 160),

    // Misc.
    TITLE_STUFF(0x8CE1E9, true, false, 32),
    INTRO1(0x8CE4C9, true, false, 32),
    INTRO2(0x8CE3E9, true, false, 256),
    INTRO3(0x8CE569, true, false, 32),
    PAUSE_SCREEN(0xB6F000, true, false, 512),
    TITLE_METROID(0x8BA2B9, true, false, 128),

    BEAM(0x90C3E1, true, false, 32),
    SPORE_SPAWN_DAMAGE_GRADIENTS(0xA5E359, true, false, 32),
    SPORE_SPAWN_FOREGROUND_GRADIENTS(0xA5E4F9, true, false, 32),
    SPORE_SPAWN_BACKGROUND_GRADIENTS(0xA5E5D9, true, false, 32),
    GOLD_TORIZO(0xAA8787, true, false, 32),
    PALETTE_BLEND(0x89AA02, true, false, 32),

    NINTENDO_LOGO(0x8CE2E9, true, false, 32),

    CREDITS(0x8CE9E9, true, false, 0xFE),
    RATE_FOR_ITEM_COLLECTION(0x8CE8A9, true, false, 0xFE),

    SAMUS_ENDING1(0x8CE9A9, true, false, 32),
    SAMUS_ENDING2(0x9A8140, true, false, 32),
    SAMUS_ENDING3(0x8CE829, true, false, 32),

//    MISC(0x9A81A0, true, false, 20000);
    ;
    public final int address;
    public final boolean direct;
    public final boolean compressed;
    public final int length;

    PaletteEnum(int address, boolean direct, boolean compressed, int length) {
        this.address = address;
        this.direct = direct;
        this.compressed = compressed;
        this.length = length;
    }

    public static PaletteEnum getPaletteEnum(int address) {
        for (PaletteEnum paletteEnum : values()) {
            if (paletteEnum.address == address) {
                return paletteEnum;
            }
        }
        return null;
    }
}
