package com.ostsoft.games.jsm.animation.samus.pose;

import com.ostsoft.games.jsm.util.ByteStream;

public class Transition {
    public int newButtons;
    public int currentButtons;
    public int newPosition;

    public Transition read(ByteStream stream) {
        newButtons = stream.readReversedUnsignedShort();
        currentButtons = stream.readReversedUnsignedShort();
        newPosition = stream.readReversedUnsignedShort();
        return this;
    }

}
