package com.ostsoft.games.jsm.srm;

import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.ByteStream;

public class SRM implements Storable {
    private Game[] games;

    public void newSRM() {
        games = new Game[3];
        for (int i = 0; i < 3; i++) {
            games[i] = new Game(0x10 + 0x65C * i);
        }
    }

    @Override
    public void load(ByteStream stream) {
        games = new Game[3];
        for (int i = 0; i < games.length; i++) {
            games[i] = new Game(0x10 + 0x65C * i);
            games[i].load(stream);
        }

    }

    @Override
    public void save(ByteStream stream) {
        for (Game game : games) {
            game.save(stream);
        }
        for (int i = 0; i < 3; i++) {
            fixChecksum(stream, i);
        }
    }

    private void fixChecksum(ByteStream stream, int gameIndex) {
        stream.setPosition(0x10 + (gameIndex * 0x65));
        int high = 0;
        int low = 0;
        for (int i = 0; i < 0x65C; i++) {
            high += stream.readUnsignedByte();
            if (high > 0xFF) {
                high = high & 0xFF;
                low++;
            }
            i++;
            low += stream.readUnsignedByte();
            if (low > 0xFF) {
                low = low & 0xFF;
            }
        }
        stream.setPosition(gameIndex * 2);
        stream.writeUnsignedByte(high);
        stream.writeUnsignedByte(low);

        stream.setPosition(8 + gameIndex * 2);
        stream.writeUnsignedByte(high ^ 0xFF);
        stream.writeUnsignedByte(low ^ 0xFF);

        stream.setPosition(0x1FF0 + gameIndex * 2);
        stream.writeUnsignedByte(high);
        stream.writeUnsignedByte(low);

        stream.setPosition(0x1FF8 + gameIndex * 2);
        stream.writeUnsignedByte(high ^ 0xFF);
        stream.writeUnsignedByte(low ^ 0xFF);


    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public void setOffset(int offset) {

    }

    @Override
    public int getSize() {
        return 0x2000;
    }

    public Game[] getGames() {
        return games;
    }
}
