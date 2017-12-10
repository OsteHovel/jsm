package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class Layer1And2 {
    public int header;
    public List<Tile> tiles;
    public List<Tile> layer2Tiles;
    public List<Integer> btsList;

    public Layer1And2(byte[] data) {
        ByteStream stream = new ByteStream(data);

        header = stream.readReversedUnsignedShort();
        tiles = new ArrayList<>();
        btsList = new ArrayList<>();

        int tileLength = (header / 2);
        for (int i = 0; i < tileLength; i++) {
            tiles.add(new Tile(stream));
        }

        for (int i = 0; i < tileLength; i++) {
            btsList.add(stream.readUnsignedByte());
        }

        if (data.length - ((tiles.size() + 1) * 2) - (btsList.size() + 1) - 2 > 0) {
            layer2Tiles = new ArrayList<>();
            for (int i = 0; i < tileLength; i++) {
                layer2Tiles.add(new Tile(stream));
            }
        }
    }

    public void printDebug() {

    }
}
