package com.ostsoft.games.jsm.animation.samus;

public enum AnimationTerminator {
    // http://jathys.zophar.net/supermetroid/kejardon/RandomRoutines.txt
    STOP_AT_LAST_FRAME_0(0xF0, 0),
    STOP_AT_LAST_FRAME_1(0xF1, 0),
    STOP_AT_LAST_FRAME_2(0xF2, 0),
    STOP_AT_LAST_FRAME_3(0xF3, 0),
    STOP_AT_LAST_FRAME_4(0xF4, 0),
    STOP_AT_LAST_FRAME_5(0xF5, 0),
    HEAVY_BREATHING(0xF6, 0),
    EXHAUSTED(0xF7, 0),
    UNKNOWN_CHECKS(0xF8, 1),
    EQUIPMENT_CHECKS(0xF9, 6),
    NOT_FALLING_USE_NEXT_AS_ARGUMENT(0xFA, 2),
    WALL_JUMP_ONLY(0xFB, 0),
    CHECK_EQUIPMENT(0xFC, 4),
    USE_NEXT_BYTE_IN_ANIMATION_LOOP(0xFD, 5),
    BACKTRACK_FRAMES(0xFE, 1),
    LOOP(0xFF, 0);

    public final int value;
    public final int length;

    AnimationTerminator(int value, int length) {
        this.value = value;
        this.length = length;
    }

    public static AnimationTerminator getAnimationTerminator(int value) {
        for (AnimationTerminator animationTerminator : values()) {
            if (animationTerminator.value == value) {
                return animationTerminator;
            }
        }
        return null;
    }
}
