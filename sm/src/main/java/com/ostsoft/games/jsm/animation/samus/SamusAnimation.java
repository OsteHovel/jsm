package com.ostsoft.games.jsm.animation.samus;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.FrameManager;
import com.ostsoft.games.jsm.animation.SpriteMapManager;
import com.ostsoft.games.jsm.animation.samus.dma.DMATables;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;

public class SamusAnimation extends Animation {
    private final DMATables dmaTables;
    private final SpriteMapManager spriteMapManager;
    private final FrameManager frameManager;
    private final int poseIndex;

    public int numberOfFrames;
    public AnimationTerminator animationTerminator;
    public byte[] animationTerminatorBytes;
    private int frameSpeedPointer;

    public SamusAnimation(int poseIndex, DMATables dmaTables, SpriteMapManager spriteMapManager, FrameManager frameManager) {
        this.poseIndex = poseIndex;
        this.dmaTables = dmaTables;
        this.spriteMapManager = spriteMapManager;
        this.frameManager = frameManager;
    }

    static int getAnimationFrameSpeedPointer(ByteStream stream, int poseIndex) {
        int animationFrameSpeedPointerPointer = Pointer.SamusAnimationSpeedBaseTablePointer.pointer + poseIndex * 2;
        stream.setPosition(BitHelper.snesToPc(animationFrameSpeedPointerPointer));
        return stream.readReversedUnsignedShort() | (0x91 << 16);
    }

    public void load(ByteStream stream) {
        // Number of animation frames
        frameSpeedPointer = getAnimationFrameSpeedPointer(stream, getPoseIndex());
        stream.setPosition(BitHelper.snesToPc(frameSpeedPointer));
        int animationDuration = stream.readUnsignedByte();
        numberOfFrames = 0;
        while (animationDuration < 0xF0) {
            numberOfFrames++;
            animationDuration = stream.readUnsignedByte();
        }
        animationTerminator = AnimationTerminator.getAnimationTerminator(animationDuration);
        animationTerminatorBytes = new byte[6];
        stream.read(animationTerminatorBytes);

        // Load frames
        animationFrames = new ArrayList<>();
        for (int i = 0; i < numberOfFrames; i++) {
            SamusFrame samusFrame = new SamusFrame(dmaTables, spriteMapManager, frameManager, getPoseIndex(), i);
            samusFrame.load(stream);
            animationFrames.add(samusFrame);
        }
    }

    @Override
    public void save(ByteStream stream) {
//        setAnimationFrameSpeedPointer(stream, getPoseIndex(), frameSpeedPointer);
//        stream.setPosition(BitHelper.snesToPc(frameSpeedPointer));
//        stream.seek(animationFrames.size());
//        stream.writeUnsignedByte(animationTerminator.value);
//        stream.writeBytes(animationTerminatorBytes);

        for (AnimationFrame animationFrame : animationFrames) {
            animationFrame.save(stream);
        }
    }

    private void setAnimationFrameSpeedPointer(ByteStream stream, int poseIndex, int address) {
        int animationFrameSpeedPointerPointer = Pointer.SamusAnimationSpeedBaseTablePointer.pointer + poseIndex * 2;
        stream.setPosition(BitHelper.snesToPc(animationFrameSpeedPointerPointer));
        stream.writeUnsignedReversedShort(address);
    }

    public int getPoseIndex() {
        return poseIndex;
    }

    public int getFrameSpeedPointer() {
        return frameSpeedPointer;
    }

    public void setFrameSpeedPointer(int frameSpeedPointer) {
        this.frameSpeedPointer = frameSpeedPointer;
    }


    @Override
    public String toString() {
        String poseList[] = new String[]{
                "00: Facing forward, ala Elevator pose.",
                "01: Facing right, normal",
                "02: Facing left, normal",
                "03: Facing right, aiming up",
                "04: Facing left, aiming up",
                "05: Facing right, aiming upright",
                "06: Facing left, aiming upleft",
                "07: Facing right, aiming downright",
                "08: Facing left, aiming downleft",
                "09: Moving right, not aiming",
                "0A: Moving left, not aiming",
                "0B: Moving right, gun extended forward (not aiming)",
                "0C: Moving left, gun extended forward (not aiming)",
                "0D: Moving right, aiming straight up (unused?)",
                "0E: Moving left, aiming straight up (unused?)",
                "0F: Moving right, aiming upright",
                "10: Moving left, aiming upleft",
                "11: Moving right, aiming downright",
                "12: Moving left, aiming downleft",
                "13: Normal jump facing right, gun extended, not aiming or moving",
                "14: Normal jump facing left, gun extended, not aiming or moving",
                "15: Normal jump facing right, aiming up",
                "16: Normal jump facing left, aiming up",
                "17: Normal jump facing right, aiming down",
                "18: Normal jump facing left, aiming down",
                "19: Spin Jump right",
                "1A: Spin Jump left",
                "1B: Space jump right",
                "1C: Space jump left",
                "1D: Facing right as morphball, no springball",
                "1E: Moving right as a morphball on ground without springball",
                "1F: Moving left as a morphball on ground without springball",
                "20: Spinjump right. Unused?",
                "21: Spinjump right. Unused?",
                "22: Spinjump right. Unused?",
                "23: Spinjump right. Unused?",
                "24: Spinjump right. Unused?",
                "25: Starting standing right, turning left",
                "26: Starting standing left, turning right",
                "27: Crouching, facing right",
                "28: Crouching, facing left",
                "29: Falling facing right, normal pose",
                "2A: Falling facing left, normal pose",
                "2B: Falling facing right, aiming up",
                "2C: Falling facing left, aiming down",
                "2D: Falling facing right, aiming down",
                "2E: Falling facing left, aiming down",
                "2F: Starting with normal jump facing right, turning left",
                "30: Starting with normal jump facing left, turning right",
                "31: Midair morphball facing right without springball",
                "32: Midair morphball facing left without springball",
                "33: Spinjump right. Unused?",
                "34: Spinjump right. Unused?",
                "35: Crouch transition, facing right",
                "36: Crouch transition, facing left",
                "37: Morphing into ball, facing right. Ground and mid-air",
                "38: Morphing into ball, facing left. Ground and mid-air",
                "39: Midair morphing into ball, facing right? May be unused",
                "3A: Midair morphing into ball, facing left? May be unused",
                "3B: Standing from crouching, facing right",
                "3C: Standing from crouching, facing left",
                "3D: Demorph while facing right. Mid-air and on ground",
                "3E: Demorph while facing left. Mid-air and on ground",
                "3F: Something with morphball, facing right. Maybe unused",
                "40: Something with morphball, facing left. Maybe unused",
                "41: Staying still with morphball, facing left, no springball",
                "42: Spinjump right. Unused?",
                "43: Starting from crouching right, turning left",
                "44: Starting from crouching left, turning right",
                "45: Standing, facing right, shooting left. Unused?",
                "46: Standing, facing left, shooting right. Unused?",
                "47: Standing, facing right. Unused?",
                "48: Standing, facing left. Unused?",
                "49: Moonwalk, facing left",
                "4A: Moonwalk, facing right",
                "4B: Normal jump transition from ground(standing or crouching), facing right",
                "4C: Normal jump transition from ground(standing or crouching), facing left",
                "4D: Normal jump facing right, gun not extended, not aiming, not moving",
                "4E: Normal jump facing left, gun not extended, not aiming, not moving",
                "4F: Hurt roll back, moving right/facing left",
                "50: Hurt roll back, moving left/facing right",
                "51: Normal jump facing right, moving forward (gun extended)",
                "52: Normal jump facing left, moving forward (gun extended)",
                "53: Hurt, facing right",
                "54: Hurt, facing left",
                "55: Normal jump transition from ground, facing right and aiming up",
                "56: Normal jump transition from ground, facing left and aiming up",
                "57: Normal jump transition from ground, facing right and aiming upright",
                "58: Normal jump transition from ground, facing left and aiming upleft",
                "59: Normal jump transition from ground, facing right and aiming downright",
                "5A: Normal jump transition from ground, facing left and aiming downleft",
                "5B: Something for grapple (wall jump?), probably unused",
                "5C: Something for grapple (wall jump?), probably unused",
                "5D: Broken grapple? Facing clockwise, maybe unused",
                "5E: Broken grapple? Facing clockwise, maybe unused",
                "5F: Broken grapple? Facing clockwise, maybe unused",
                "60: Better broken grapple. Facing clockwise, maybe unused",
                "61: Nearly normal grapple. Facing clockwise, maybe unused",
                "62: Nearly normal grapple. Facing counterclockwise, maybe unused",
                "63: Facing left on grapple blocks, ready to jump. Unused?",
                "64: Facing right on grapple blocks, ready to jump. Unused?",
                "65: Glitchy jump, facing left. Used by unused grapple jump?",
                "66: Glitchy jump, facing right. Used by unused grapple jump?",
                "67: Facing right, falling, fired a shot",
                "68: Facing left, falling, fired a shot",
                "69: Normal jump facing right, aiming upright. Moving optional",
                "6A: Normal jump facing left, aiming upleft. Moving optional",
                "6B: Normal jump facing right, aiming downright. Moving optional",
                "6C: Normal jump facing left, aiming downleft. Moving optional",
                "6D: Falling facing right, aiming upright",
                "6E: Falling facing left, aiming upleft",
                "6F: Falling facing right, aiming downright",
                "70: Falling facing left, aiming downleft",
                "71: Standing to crouching, facing right and aiming upright",
                "72: Standing to crouching, facing left and aiming upleft",
                "73: Standing to crouching, facing right and aiming downright",
                "74: Standing to crouching, facing left and aiming downleft",
                "75: Moonwalk, facing left aiming upleft",
                "76: Moonwalk, facing right aiming upright",
                "77: Moonwalk, facing left aiming downleft",
                "78: Moonwalk, facing right aiming downright",
                "79: Spring ball on ground, facing right",
                "7A: Spring ball on ground, facing left",
                "7B: Spring ball on ground, moving right",
                "7C: Spring ball on ground, moving left",
                "7D: Spring ball falling, facing/moving right",
                "7E: Spring ball falling, facing/moving left",
                "7F: Spring ball jump in air, facing/moving right",
                "80: Spring ball jump in air, facing/moving left",
                "81: Screw attack right",
                "82: Screw attack left",
                "83: Walljump right",
                "84: Walljump left",
                "85: Crouching, facing right aiming up",
                "86: Crouching, facing left aiming up",
                "87: Turning from right to left while falling",
                "88: Turning from left to right while falling",
                "89: Ran into a wall on right (facing right)",
                "8A: Ran into a wall on left (facing left)",
                "8B: Turn around while standing. Probably used, but not sure when. TODO",
                "8C: Turn around while standing. Probably used, but not sure when. TODO",
                "8D: Turn around from right to left while aiming diagonal down while standing",
                "8E: Turn around from left to right while aiming diagonal down while standing",
                "8F: Turning around from right to left while aiming straight up in midair",
                "90: Turning around from left to right while aiming straight up in midair",
                "91: Turning around from right to left while aiming down or diagonal down in midair",
                "92: Turning around from left to right while aiming down or diagonal down in midair",
                "93: Turning around from right to left while aiming straight up while falling",
                "94: Turning around from left to right while aiming straight up while falling",
                "95: Turning around from right to left while aiming down or diagonal down while falling",
                "96: Turning around from left to right while aiming down or diagonal down while falling",
                "97: Turning around from right to left while aiming straight up while crouching",
                "98: Turning around from left to right while aiming straight up while crouching",
                "99: Turning around from right to left while aiming diagonal down while crouching",
                "9A: Turning around from left to right while aiming diagonal down while crouching",
                "9B: Facing forward, ala Elevator pose... with the Varia and/or Gravity Suit.",
                "9C: Turning around from right to left while aiming diagonal up while standing",
                "9D: Turning around from left to right while aiming diagonal up while standing",
                "9E: Turning around from right to left while aiming diagonal up in midair",
                "9F: Turning around from left to right while aiming diagonal up in midair",
                "A0: Turning around from right to left while aiming diagonal up while falling",
                "A1: Turning around from left to right while aiming diagonal up while falling",
                "A2: Turn around from right to left while aiming diagonal up while crouching",
                "A3: Turn around from left to right while aiming diagonal up while crouching",
                "A4: Landing from normal jump, facing right",
                "A5: Landing from normal jump, facing left",
                "A6: Landing from spin jump, facing right",
                "A7: Landing from spin jump, facing left",
                "A8: Just standing, facing right. Unused?",
                "A9: Just standing, facing left. Unused?",
                "AA: Just standing, facing right aiming downright. Unused?",
                "AB: Just standing, facing left aiming downleft. Unused?",
                "AC: Jumping, facing right, gun extended. Unused?",
                "AD: Jumping, facing left, gun extended. Unused?",
                "AE: Jumping, facing right, aiming down. Unused?",
                "AF: Jumping, facing left, aiming down. Unused?",
                "B0: Jumping, facing right, aiming downright. Unused?",
                "B1: Jumping, facing left, aiming downleft. Unused?",
                "B2: Grapple, facing clockwise",
                "B3: Grapple, facing counterclockwise",
                "B4: Crouching, facing right. Unused?",
                "B5: Crouching, facing left. Unused?",
                "B6: Crouching, facing right, aiming downright. Unused?",
                "B7: Crouching, facing left, aiming downleft. Unused?",
                "B8: Grapple, attached to a wall on right, facing left",
                "B9: Grapple, attached to a wall on left, facing right",
                "BA: Grabbed by Draygon, facing left, not moving",
                "BB: Grabbed by Draygon, facing left aiming upleft, not moving",
                "BC: Grabbed by Draygon, facing left and firing",
                "BD: Grabbed by Draygon, facing left aiming downleft, not moving",
                "BE: Grabbed by Draygon, facing left, moving",
                "BF: Turn around while standing. Probably used, but not sure when. TODO",
                "C0: Turn around while standing. Probably used, but not sure when. TODO",
                "C1: Turn around while standing. Probably used, but not sure when. TODO",
                "C2: Turn around while standing. Probably used, but not sure when. TODO",
                "C3: Turn around while standing. Probably used, but not sure when. TODO",
                "C4: Turn around while standing. Probably used, but not sure when. TODO",
                "C5: Morph ball, facing right. Unused?",
                "C6: Morph ball, facing left. Unused?",
                "C7: Super jump windup, facing right",
                "C8: Super jump windup, facing left",
                "C9: Horizontal super jump, right",
                "CA: Horizontal super jump, left",
                "CB: Vertical super jump, facing right",
                "CC: Vertical super jump, facing left",
                "CD: Diagonal super jump, right",
                "CE: Diagonal super jump, left",
                "CF: Samus ran right into a wall, is still holding right and is now aiming diagonal up",
                "D0: Samus ran right into a left, is still holding left and is now aiming diagonal up",
                "D1: Samus ran right into a wall, is still holding right and is now aiming diagonal down",
                "D2: Samus ran left into a wall, is still holding left and is now aiming diagonal down",
                "D3: Crystal flash, facing right",
                "D4: Crystal flash, facing left",
                "D5: X-raying right, standing",
                "D6: X-raying left, standing",
                "D7: Crystal flash ending, facing right",
                "D8: Crystal flash ending, facing left",
                "D9: X-raying right, crouching",
                "DA: X-raying left, crouching",
                "DB: Standing transition to morphball, facing right? Unused?",
                "DC: Standing transition to morphball, facing left? Unused?",
                "DD: Morphball transition to standing, facing right? Unused?",
                "DE: Morphball transition to standing, facing left? Unused?",
                "DF: Samus is facing left as a morphball. Unused?",
                "E0: Landing from normal jump, facing right and aiming up",
                "E1: Landing from normal jump, facing left and aiming up",
                "E2: Landing from normal jump, facing right and aiming upright",
                "E3: Landing from normal jump, facing left and aiming upleft",
                "E4: Landing from normal jump, facing right and aiming downright",
                "E5: Landing from normal jump, facing left and aiming downleft",
                "E6: Landing from normal jump, facing right, firing",
                "E7: Landing from normal jump, facing left, firing",
                "E8: Samus exhausted(Metroid drain, MB attack), facing right",
                "E9: Samus exhausted(Metroid drain, MB attack), facing left",
                "EA: Samus exhausted, looking up to watch Metroid attack MB, facing right",
                "EB: Samus exhausted, looking up to watch Metroid attack MB, facing left",
                "EC: Grabbed by Draygon, facing right. Not moving",
                "ED: Grabbed by Draygon, facing right aiming upright. Not moving",
                "EC: Grabbed by Draygon, facing right and firing.",
                "EF: Grabbed by Draygon, facing right aiming downright. Not moving",
                "F0: Grabbed by Draygon, facing right. Moving",
                "F1: Crouch transition, facing right and aiming up",
                "F2: Crouch transition, facing left and aiming up",
                "F3: Crouch transition, facing right and aiming upright",
                "F4: Crouch transition, facing left and aiming upleft",
                "F5: Crouch transition, facing right and aiming downright",
                "F6: Crouch transition, facing left and aiming downleft",
                "F7: Crouching to standing, facing right and aiming up",
                "F8: Crouching to standing, facing left and aiming upleft",
                "F9: Crouching to standing, facing right and aiming upright",
                "FA: Crouching to standing, facing left and aiming upleft",
                "FB: Crouching to standing, facing right and aiming downright",
                "FC: Crouching to standing, facing left and aiming downleft"
        };
        return poseList[poseIndex];
    }
}
