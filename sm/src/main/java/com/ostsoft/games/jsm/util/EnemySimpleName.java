package com.ostsoft.games.jsm.util;

public enum EnemySimpleName {
    // Enemies
    SHUTTER3(0xD83F), // Fake Kamer

    WINGS_OF_HACHI1(0xEAFF),
    WINGS_OF_HACHI2(0xEB7F),

    // Bosses
    SPORE_SPAWN(0xDF3F),

    // Escape
    ESCAPE_SMOKE(0xE1FF),

    // Misc
    ELEVATOR(0xD73F),
    SHIP_PART1(0xD07F),
    SHIP_PART2(0xD0BF);


    public final int value;

    EnemySimpleName(int value) {
        this.value = value;
    }

    public static EnemySimpleName getSimpleName(int value) {
        for (EnemySimpleName enemySimpleName : values()) {
            if (enemySimpleName.value == value) {
                return enemySimpleName;
            }
        }
        return null;
    }
}
