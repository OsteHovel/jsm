package com.ostsoft.games.jsm.layer3;

public enum Layer3Type {
    NONE(0x00),
    LAVA(0x02),
    ACID(0x04),
    WATER(0x06),
    SPORES(0x08),
    RAIN(0x0A),
    FOG(0x0C),
    SKY_SCROLLING(0x20),
    UNUSED(0x22),
    FIREFLEA_FX(0x24),
    FOUR_STATUES(0x26),
    CERES_ELEVATOR(0x28),
    CERES_RIDLY(0x2A),
    HAZE(0x2C);


    public final int value;

    Layer3Type(int value) {
        this.value = value;
    }

    public static Layer3Type getLayer3Type(int value) {
        for (Layer3Type layer3Type : values()) {
            if (layer3Type.value == value) {
                return layer3Type;
            }
        }
        return null;
    }
}
