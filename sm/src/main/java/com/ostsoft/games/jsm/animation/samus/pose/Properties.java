package com.ostsoft.games.jsm.animation.samus.pose;

import com.ostsoft.games.jsm.util.ByteStream;

public class Properties {
    public int samusDirection; // samDir in SMILE
    public int movementType; // moveType in SMILE
    public int newPose;
    public int shootDirection; // shotDir in SMILE
    public int verticalDisplace; // verDisplace in SMILE
    public int unused1;
    public int verticalRadius; // verRadius in SMILE
    public int unused2;


    public Properties read(ByteStream stream) {
        samusDirection = stream.readUnsignedByte();
        movementType = stream.readUnsignedByte();
        newPose = stream.readUnsignedByte();
        shootDirection = stream.readUnsignedByte();
        verticalDisplace = stream.readUnsignedByte();
        unused1 = stream.readUnsignedByte();
        verticalRadius = stream.readUnsignedByte();
        unused2 = stream.readUnsignedByte();

        return this;
    }

    public void printDebug() {
        System.out.println("[PoseProperties] " + samusDirection);
    }
}
