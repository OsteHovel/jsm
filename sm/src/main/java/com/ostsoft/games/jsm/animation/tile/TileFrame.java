package com.ostsoft.games.jsm.animation.tile;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;

public class TileFrame extends AnimationFrame {
    public final int index;
    public int gfxPointer;

    public TileFrame(int index) {
        this.index = index;
    }

    public void load(ByteStream stream) {
        setFrameLength(stream.readReversedUnsignedShort());
        gfxPointer = stream.readReversedUnsignedShort();

        stream.setPosition(BitHelper.snesToPc(gfxPointer | (0x87 << 16)));
        byte[] tiles = new byte[1024];
        stream.read(tiles);

        setTilesBytes(tiles);


        int height = 0;
        int width = 0;
        int[] tilePlacement = new int[0];
        switch (index) {
            case 1:
                height = 2;
                width = 2;
                tilePlacement = new int[]{0, 1, 2, 3};
                break;
            case 2:
                height = 2;
                width = 2;
                tilePlacement = new int[]{0, 1, 2, 3};
                break;
            case 3:
                height = 4;
                width = 4;
                tilePlacement = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
                break;
            case 7:
                height = 1;
                width = 2;
                tilePlacement = new int[]{0, 0};
                break;
            case 8:
                height = 1;
                width = 2;
                tilePlacement = new int[]{0, 0};
                break;
            case 9:
                height = 2;
                width = 4;
                tilePlacement = new int[]{0, 0, 1, 2, 3, 4, 5, 6};
                break;


        }

        spriteMapEntries = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                SpriteMapEntry spriteMapEntry = new SpriteMapEntry();
                spriteMapEntry.setX(x * 8);
                spriteMapEntry.setY(y * 8);
                spriteMapEntry.setTileIndex(tilePlacement[(y * width + x)]);
                spriteMapEntries.add(spriteMapEntry);
            }
        }
    }

    public void printDebug() {
        System.out.println("AnimationGFXFrame: FrameCount: 0x" + Integer.toHexString(getFrameLength()).toUpperCase() + " - " + getFrameLength() + " gfxPointer: 0x" + Integer.toHexString(gfxPointer).toUpperCase());
    }


    @Override
    public void save(ByteStream byteStream) {

    }
}
