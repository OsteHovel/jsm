package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;

public class EnemyLocation {
    public int enemyDataPointer;
    public int x;
    public int y;
    public int graphicsPointer;
    public int propertyBits;
    public int morePropertyBits;
    public int roomArgument1;
    public int roomArgument2;

    public EnemyLocation(ByteStream stream) {
        enemyDataPointer = stream.readReversedUnsignedShort() | (0xA0 << 16);
        x = stream.readReversedUnsignedShort();
        y = stream.readReversedUnsignedShort();
        graphicsPointer = stream.readReversedUnsignedShort();
        propertyBits = stream.readReversedUnsignedShort();
        morePropertyBits = stream.readReversedUnsignedShort();
        roomArgument1 = stream.readReversedUnsignedShort();
        roomArgument2 = stream.readReversedUnsignedShort();
    }
}
