package com.ostsoft.games.jsm.projectile;


import com.ostsoft.games.jsm.util.ByteStream;

public class DamageAndDirection {
    public int damage;
    public int directionPointers[];

    public DamageAndDirection(ByteStream stream) {
        damage = stream.readReversedUnsignedShort();
        directionPointers = new int[10];
        for (int i = 0; i < directionPointers.length; i++) {
            directionPointers[i] = stream.readReversedUnsignedShort() | (0x93 << 16);
        }


    }

    public void printDebug() {
        System.out.println("[DamageAndDirection] Damage: " + damage);
    }

    public enum Direction {
        FACING_RIGHT_SHOOTING_UP(0x0),
        FACING_RIGHT_SHOOTING_UP_RIGHT(0x1),
        FACING_RIGHT_SHOOTING_RIGHT(0x2),
        FACING_RIGHT_SHOOTING_DOWN_RIGHT(0x3),
        FACING_RIGHT_SHOOTING_DOWN(0x4),
        FACING_LEFT_SHOOTING_DOWN(0x5),
        FACING_LEFT_SHOOTING_DOWN_LEFT(0x6),
        FACING_LEFT_SHOOTING_LEFT(0x7),
        FACING_LEFT_SHOOTING_UP_LEFT(0x8),
        FACING_LEFT_SHOOTING_UP(0x9);

        public final int value;

        Direction(int value) {
            this.value = value;
        }

        public static Direction getDirection(int value) {
            for (Direction direction : values()) {
                if (direction.value == value) {
                    return direction;
                }
            }
            return null;
        }
    }
}
