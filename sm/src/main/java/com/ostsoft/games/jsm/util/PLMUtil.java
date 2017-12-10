package com.ostsoft.games.jsm.util;

public class PLMUtil {
    private PLMUtil() {

    }

    public enum PLMMajorType {
        DOOR,
        SCROLL,
        ITEM,
        MISC,
    }


    public static enum PLMType {
        GREY_DOOR_UNKNOWN_FIX_ME(0xC84F, PLMMajorType.MISC, null),


        // Doors
        PURPLE_DOOR_UP(0xC89C, PLMMajorType.DOOR, Direction.UP),
        PURPLE_DOOR_DOWN(0xC896, PLMMajorType.DOOR, Direction.DOWN),
        PURPLE_DOOR_LEFT(0xC890, PLMMajorType.DOOR, Direction.LEFT),
        PURPLE_DOOR_RIGHT(0xC88A, PLMMajorType.DOOR, Direction.RIGHT),

        GREEN_DOOR_UP(0x884, PLMMajorType.DOOR, Direction.UP),
        GREEN_DOOR_DOWN(0xC87E, PLMMajorType.DOOR, Direction.DOWN),
        GREEN_DOOR_LEFT(0xC878, PLMMajorType.DOOR, Direction.LEFT),
        GREEN_DOOR_RIGHT(0xC872, PLMMajorType.DOOR, Direction.RIGHT),

        YELLOW_DOOR_UP(0xC86C, PLMMajorType.DOOR, Direction.UP),
        YELLOW_DOOR_DOWN(0xC866, PLMMajorType.DOOR, Direction.DOWN),
        YELLOW_DOOR_LEFT(0x860, PLMMajorType.DOOR, Direction.LEFT),
        YELLOW_DOOR_RIGHT(0xC85A, PLMMajorType.DOOR, Direction.RIGHT),

        GREY_DOOR_UP(0xC84E, PLMMajorType.DOOR, Direction.UP),
        GREY_DOOR_UP2(0xC854, PLMMajorType.DOOR, Direction.UP),
        GREY_DOOR_LEFT(0xBAF4, PLMMajorType.DOOR, Direction.LEFT),
        GREY_DOOR_RIGHT(0xC842, PLMMajorType.DOOR, Direction.RIGHT),
        GREY_DOOR_LEFT2(0xC848, PLMMajorType.DOOR, Direction.LEFT),
        GREY_DOOR_DOWN(0xC84E, PLMMajorType.DOOR, Direction.DOWN),

        EYE_DOOR_PIECE_LEFT1(0xDB4C, PLMMajorType.MISC, Direction.LEFT),
        EYE_DOOR_PIECE_LEFT2(0xDB48, PLMMajorType.MISC, Direction.LEFT),
        EYE_DOOR_PIECE_LEFT3(0xDB52, PLMMajorType.MISC, Direction.LEFT),

        EYE_DOOR_PIECE_RIGHT1(0xDB5A, PLMMajorType.MISC, Direction.RIGHT),
        EYE_DOOR_PIECE_RIGHT2(0xDB56, PLMMajorType.MISC, Direction.RIGHT),
        EYE_DOOR_PIECE_RIGHT3(0xDB60, PLMMajorType.MISC, Direction.RIGHT),

        // Scroll
        SCROLL(0xB703, PLMMajorType.SCROLL, null),
        SCROLL_COPY_PREVIOUS_MOVES_HORIZONTALLY(0xB63B, PLMMajorType.SCROLL, null),
        SCROLL_COPY_PREVIOUS_MOVES_VERTICALLY(0xB647, PLMMajorType.SCROLL, null),

        // Item
        ITEM(0xEEBD, PLMMajorType.ITEM, null),

        ENERGY_TANK(0xEED7, PLMMajorType.ITEM, null),
        ENERGY_TANK_CHOZO_BALL(0xEF2B, PLMMajorType.ITEM, null),
        ENERGY_TANK_HIDDEN_SCENERY(0xEF7F, PLMMajorType.ITEM, null),

        RESERVE_TANK(0xEF27, PLMMajorType.ITEM, null),
        RESERVE_TANK_CHOZO_BALL(0xEF7B, PLMMajorType.ITEM, null),
        RESERVE_TANK_HIDDEN_SCENERY(0xEFCF, PLMMajorType.ITEM, null),

        MISSILE(0xEEDB, PLMMajorType.ITEM, null),
        MISSILE_CHOZO_BALL(0xEF2F, PLMMajorType.ITEM, null),
        MISSILE_HIDDEN_SCENERY(0xEF83, PLMMajorType.ITEM, null),

        SUPER_MISSILE(0xEEDF, PLMMajorType.ITEM, null),
        SUPER_MISSILE_CHOZO_BALL(0xEF33, PLMMajorType.ITEM, null),
        SUPER_MISSILE_HIDDEN_SCENERY(0xEF87, PLMMajorType.ITEM, null),

        POWER_BOMB(0xEEE3, PLMMajorType.ITEM, null),
        POWER_BOMB_CHOZO_BALL(0xEF37, PLMMajorType.ITEM, null),
        POWER_BOMB_HIDDEN_SCENERY(0xEF8B, PLMMajorType.ITEM, null),

        // Beams
        CHARGE_BEAM(0xEEEB, PLMMajorType.ITEM, null),
        CHARGE_BEAM_CHOZO_BALL(0xEF3F, PLMMajorType.ITEM, null),
        CHARGE_BEAM_HIDDEN_SCENERY(0xEF93, PLMMajorType.ITEM, null),

        ICE_BEAM(0xEEEF, PLMMajorType.ITEM, null),
        ICE_BEAM_CHOZO_BALL(0xEF43, PLMMajorType.ITEM, null),
        ICE_BEAM_HIDDEN_SCENERY(0xEF97, PLMMajorType.ITEM, null),

        SPAZER(0xEEFF, PLMMajorType.ITEM, null),
        SPAZER_CHOZO_BALL(0xEF53, PLMMajorType.ITEM, null),
        SPAZER_HIDDEN_SCENERY(0xEFA7, PLMMajorType.ITEM, null),

        WAVE_BEAM(0xEEFB, PLMMajorType.ITEM, null),
        WAVE_BEAM_CHOZO_BALL(0xEF4F, PLMMajorType.ITEM, null),
        WAVE_BEAM_HIDDEN_SCENERY(0xEFA3, PLMMajorType.ITEM, null),

        PLASMA_BEAM(0xEF13, PLMMajorType.ITEM, null),
        PLASMA_BEAM_CHOZO_BALL(0xEF67, PLMMajorType.ITEM, null),
        PLASMA_BEAM_HIDDEN_SCENERY(0xEFBB, PLMMajorType.ITEM, null),

        GRAPPLE_BEAM(0xEF17, PLMMajorType.ITEM, null),
        GRAPPLE_BEAM_CHOZO_BALL(0xEF6B, PLMMajorType.ITEM, null),
        GRAPPLE_BEAM_HIDDEN_SCENERY(0xEFBF, PLMMajorType.ITEM, null),

        XRAY(0xEF0F, PLMMajorType.ITEM, null),
        XRAY_CHOZO_BALL(0xEF63, PLMMajorType.ITEM, null),
        XRAY_HIDDEN_SCENERY(0xEFB7, PLMMajorType.ITEM, null),

        // Equipment
        MORPHINGBALL(0xEF23, PLMMajorType.ITEM, null),
        MORPHINGBALL_CHOZO_BALL(0xEF77, PLMMajorType.ITEM, null),
        MORPHINGBALL_HIDDEN_SCENERY(0xEFCB, PLMMajorType.ITEM, null),

        BOMBS_CHOZO_BALL(0xEF3B, PLMMajorType.ITEM, null),
        BOMBS_HIDDEN_SCENERY(0xEF8F, PLMMajorType.ITEM, null),

        VARIA_SUIT(0xEF07, PLMMajorType.ITEM, null),
        VARIA_SUIT_CHOZO_BALL(0xEF5B, PLMMajorType.ITEM, null),
        VARIA_SUIT_HIDDEN_SCENERY(0xEFAF, PLMMajorType.ITEM, null),

        GRAVITY_SUIT(0xEF0B, PLMMajorType.ITEM, null),
        GRAVITY_SUIT_CHOZO_BALL(0xEF5F, PLMMajorType.ITEM, null),
        GRAVITY_SUIT_HIDDEN_SCENERY(0xEFB3, PLMMajorType.ITEM, null),

        SPACE_JUMP(0xEF1B, PLMMajorType.ITEM, null),
        SPACE_JUMP_CHOZO_BALL(0xEF6F, PLMMajorType.ITEM, null),
        SPACE_JUMP_HIDDEN_SCENERY(0xEFC3, PLMMajorType.ITEM, null),

        SCREW_ATTACK(0xEF1F, PLMMajorType.ITEM, null),
        SCREW_ATTACK_CHOZO_BALL(0xEF73, PLMMajorType.ITEM, null),
        SCREW_ATTACK_HIDDEN_SCENERY(0xEFC7, PLMMajorType.ITEM, null),

        HI_JUMP(0xEEF3, PLMMajorType.ITEM, null),
        HI_JUMP_CHOZO_BALL(0xEF47, PLMMajorType.ITEM, null),
        HI_JUMP_HIDDEN_SCENERY(0xEF9B, PLMMajorType.ITEM, null),

        SPEEDBOOSTER(0xEEF7, PLMMajorType.ITEM, null),
        SPEEDBOOSTER_CHOZO_BALL(0xEF4B, PLMMajorType.ITEM, null),
        SPEEDBOOSTER_HIDDEN_SCENERY(0xEF9F, PLMMajorType.ITEM, null),

        SPRING_BALL(0xEF03, PLMMajorType.ITEM, null),
        SPRING_BALL_CHOZO_BALL(0xEF57, PLMMajorType.ITEM, null),
        SPRING_BALL_HIDDEN_SCENERY(0xEFAB, PLMMajorType.ITEM, null),


        // Misc.
        MAPPING_STATION(0xB6D3, PLMMajorType.MISC, null),

        ENERGY_REFILL_STATION(0xB6DF, PLMMajorType.MISC, null),

        MISSILE_REFILL_STATION(0xB6EB, PLMMajorType.MISC, null),

        SAVE_POINT(0xB76F, PLMMajorType.MISC, null), // Low: Index of save point in area

        ELEVATOR_BASE(0xB70B, PLMMajorType.MISC, null),

        ELECTRICAL_OUTLET_LEFT(0xDF65, PLMMajorType.MISC, Direction.LEFT),

        ELECTRICAL_OUTLET_RIGHT(0xDF7D, PLMMajorType.MISC, Direction.RIGHT),;

        public final int value;
        public final PLMMajorType majorType;
        public final Direction direction;

        PLMType(int value, PLMMajorType majorType, Direction direction) {
            this.value = value;
            this.majorType = majorType;
            this.direction = direction;
        }

        public static PLMType getPLMType(int value) {
            for (PLMType plmType : values()) {
                if (plmType.value == value) {
                    return plmType;
                }
            }
            return null;
        }
    }
}
