package com.ostsoft.games.jsm.animation.samus.dma;

import com.ostsoft.games.jsm.animation.TileManager;
import com.ostsoft.games.jsm.common.SimpleTile;
import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class DMAEntry implements Storable {
    private final TileManager tileManager;
    public int graphicsPointer;
    public int sizePart1;
    public int sizePart2;
    private int offset;
    private List<Tile> tilesPart1;
    private List<Tile> tilesPart2;

    public DMAEntry(TileManager tileManager, int offset) {
        this.tileManager = tileManager;
        this.offset = offset;
    }

    public void printDebug() {
        System.out.println(Integer.toHexString(graphicsPointer).toUpperCase() + " " +
                Integer.toHexString(sizePart1).toUpperCase() + " " +
                Integer.toHexString(sizePart2).toUpperCase()
        );
    }

    @Override
    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        graphicsPointer = stream.readReversedUnsigned3Bytes();

        sizePart1 = stream.readReversedUnsignedShort();
        sizePart2 = stream.readReversedUnsignedShort();


        int position = BitHelper.snesToPc(graphicsPointer);
        if (position >= stream.size()) {
            System.out.println("Out of bounds: " + Integer.toHexString(graphicsPointer).toUpperCase());
            System.out.println(graphicsPointer & 0xFF);
            System.out.println((graphicsPointer >> 8) & 0xFF);
            System.out.println((graphicsPointer >> 16) & 0xFF);
            tilesPart1 = new ArrayList<>();
            tilesPart2 = new ArrayList<>();
            return;
        }

        tilesPart1 = new ArrayList<>();
        for (int i = 0; i < 256; i += 32) {
            if (i > sizePart1) {
                tilesPart1.add(new SimpleTile(new byte[32]));
                continue;
            }
            int tileOffset = graphicsPointer + i;
            Tile tile = tileManager.getTile(tileOffset);
            if (tile == null) {
                byte[] tileBytes = new byte[32];
                stream.setPosition(BitHelper.snesToPc(tileOffset));
                stream.read(tileBytes);
                tile = new SimpleTile(tileBytes);
                tileManager.putTile(tileOffset, tile);
            }
            tilesPart1.add(tile);
        }

        tilesPart2 = new ArrayList<>();
        for (int i = 0; i < 256; i += 32) {
            if (i > sizePart2) {
                tilesPart2.add(new SimpleTile(new byte[32]));
                continue;
            }
            int tileOffset = graphicsPointer + sizePart1 + i;
            Tile tile = tileManager.getTile(tileOffset);
            if (tile == null) {
                byte[] tileBytes = new byte[32];
                stream.setPosition(BitHelper.snesToPc(tileOffset));
                stream.read(tileBytes);
                tile = new SimpleTile(tileBytes);
                tileManager.putTile(tileOffset, tile);
            }
            tilesPart2.add(tile);
        }
    }

    @Override
    public void save(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        stream.writeUnsignedReversed3Bytes(graphicsPointer);
        stream.writeUnsignedReversedShort(sizePart1);
        stream.writeUnsignedReversedShort(sizePart2);

        for (int i = 0; i < sizePart1; i += 32) {
            int tileOffset = graphicsPointer + i;
            stream.setPosition(BitHelper.snesToPc(tileOffset));
            stream.writeBytes(tilesPart1.get(i / 32).getBytes());
        }
        for (int i = 0; i < sizePart2; i += 32) {
            int tileOffset = graphicsPointer + sizePart1 + i;
            stream.setPosition(BitHelper.snesToPc(tileOffset));
            stream.writeBytes(tilesPart2.get(i / 32).getBytes());
        }
    }

    public List<Tile> getTilesPart1() {
        return tilesPart1;
    }

    public List<Tile> getTilesPart2() {
        return tilesPart2;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getSize() {
        return 7;
    }
}
