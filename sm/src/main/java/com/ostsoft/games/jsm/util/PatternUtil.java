package com.ostsoft.games.jsm.util;

public class PatternUtil {
    private PatternUtil() {

    }

    public static boolean isHFlip(int patternByte) {
        return (patternByte & 0x4) == 0x4;
    }

    public static boolean isVFlip(int patternByte) {
        return (patternByte & 0x8) == 0x8;
    }

    public static BlockType getBlockType(int patternByte) {
        return BlockType.getBlockType((patternByte) >> 4);
    }

    public enum BlockType {
        AIR(0x0),
        SLOPE(0x1),
        XRAY_AIR(0x2),
        TREADMILL(0x3),
        SHOOTABLE_AIR(0x4),
        H_EXTEND(0x5),
        AIR_UNKNOWN(0x6),
        BOMBABLE_AIR(0x7),
        SOLID(0x8),
        DOOR(0x9),
        SPIKE(0xA),
        CRUMBLE(0xB),
        SHOT(0xC),
        V_EXTEND(0xD),
        GRAPPLE(0xE),
        BOMB_BLOCK(0xF);

        public final int value;

        BlockType(int value) {
            this.value = value;
        }

        public static BlockType getBlockType(int value) {
            for (BlockType blockType : values()) {
                if (blockType.value == value) {
                    return blockType;
                }
            }
            return null;
        }

        public static int setBlockType(int value, BlockType blockType) {
            value = (value & 0x0F) | (blockType.value << 4);
            return value;
        }

        public int setBlockType(int patternByte) {
            patternByte = (patternByte & 0x0F) | (value << 4);
            return patternByte;
        }
    }
}
