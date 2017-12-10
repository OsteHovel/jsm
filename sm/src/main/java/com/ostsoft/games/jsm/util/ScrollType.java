package com.ostsoft.games.jsm.util;

public enum ScrollType {
    RED_SCROLL(0),
    BLUE_SCROLL(1),
    GREEN_SCROLL(2);


    public final int value;

    ScrollType(int value) {
        this.value = value;
    }

    public static ScrollType getScrollType(int value) {
        for (ScrollType scrollType : values()) {
            if (scrollType.value == value) {
                return scrollType;
            }
        }
        return null;
    }
}
