package com.ostsoft.games.jsm.room;

import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.Direction;

public class Door {
    public int roomId;
    public int bitFlags;
    public int direction;
    public int x;
    public int y;
    public int xDistance;
    public int yDistance;
    public int distanceFromDoor;
    public int scrollMapPointer;


    public Door(ByteStream stream) {
        roomId = stream.readReversedUnsignedShort() | (0x8F << 16);
        bitFlags = stream.readUnsignedByte();
        direction = stream.readUnsignedByte();
        x = stream.readUnsignedByte();
        y = stream.readUnsignedByte();
        xDistance = stream.readUnsignedByte();
        yDistance = stream.readUnsignedByte();
        distanceFromDoor = stream.readReversedUnsignedShort();
        scrollMapPointer = stream.readReversedUnsignedShort() | (0x8F << 16);
    }

    public void printDebug() {
        System.out.println("roomId: 0x" + Integer.toHexString(roomId).toUpperCase() + " - " + roomId);
        System.out.println("roomOffset: 0x" + Integer.toHexString(BitHelper.snesToPc(roomId)).toUpperCase() + " - " + BitHelper.snesToPc(roomId));
        System.out.println("bitFlags: " + Integer.toBinaryString(bitFlags));
        System.out.println("Direction: 0x" + Integer.toHexString(direction).toUpperCase() + " - " + direction);
        System.out.println("X: 0x" + Integer.toHexString(x).toUpperCase() + " - " + x);
        System.out.println("Y: 0x" + Integer.toHexString(y).toUpperCase() + " - " + y);
        System.out.println("xDistance: 0x" + Integer.toHexString(xDistance).toUpperCase() + " - " + xDistance);
        System.out.println("yDistance: 0x" + Integer.toHexString(yDistance).toUpperCase() + " - " + yDistance);
        System.out.println("DistanceFromDoor: 0x" + Integer.toHexString(distanceFromDoor).toUpperCase() + " - " + distanceFromDoor);
    }

    public boolean isChangingRegion() {
        return ((bitFlags & (1 << 6)) != 0);
    }

    public boolean isElevator() {
        return ((bitFlags & (1 << 7)) != 0);
    }

    public Direction getDirection() {
        Direction direction;
        switch (this.direction) {
            case 0: // Going right?
            case 4: // Going right
                direction = Direction.RIGHT;
                break;
            case 5: // Going left
                direction = Direction.LEFT;
                break;
            case 7: // Going up
            case 3: // Going up elevator
                direction = Direction.UP;
                break;
            case 2: // Going down elevator?
            case 6: // Going down
                direction = Direction.DOWN;
                break;
            default:
                direction = Direction.LEFT;
                break;
        }
        return direction;
    }
}
