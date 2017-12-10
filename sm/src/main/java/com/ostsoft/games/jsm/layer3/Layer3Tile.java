package com.ostsoft.games.jsm.layer3;

import java.io.DataInputStream;
import java.io.IOException;

public class Layer3Tile {
    public int tile;
    public int drawMethod;

    public Layer3Tile(DataInputStream stream) throws IOException {
        tile = stream.readUnsignedByte();
        drawMethod = stream.readUnsignedByte();
    }
}
