package com.ostsoft.games.jsm.room;

import com.ostsoft.games.jsm.util.ByteStream;

public class StateSelect {
    public int type;
    private int testLength;


    public StateSelect(ByteStream stream) {
        type = stream.readReversedUnsignedShort();
        switch (type) {
            case 0xE5E6: // Zebes not awake?
                testLength = 2;
                break;
            case 0xE5EB:
                testLength = 6;
                break;
            case 0xE5FF:
                testLength = 4;
                break;
            case 0xE612: //
                testLength = 5;
                break;
            case 0xE629:
                testLength = 5;
                break;
            case 0xE640: // Morph ball
                testLength = 4;
                break;
            case 0xE652: // Morph ball and missiles
                testLength = 4;
                break;
            case 0xE669: // Power Bombs
                testLength = 4;
                break;
            case 0xE678: // Speed Booster
                testLength = 4;
                break;
            default:
                System.out.println("ERROR IN TESTCODE: " + Integer.toHexString(type));
                System.exit(1);
                break;
        }

        stream.seek(testLength - 2);
    }

    public void printDebug() {


    }
}