package com.ostsoft.games.jsm.animation.tile;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class TileAnimation extends Animation {
    private final int tableStart;
    private final int index;

    public TileAnimation(int tableStart, int index) {
        this.tableStart = tableStart;
        this.index = index;
    }

    @Override
    public void load(ByteStream stream) {
        int bank = 0x87;

        // Read out number of frames
        stream.setPosition(BitHelper.snesToPc(tableStart));
        int numberOfFrames;
        for (numberOfFrames = 0; ((stream.readReversedUnsignedShort() & 0x8000) != 0x8000); numberOfFrames++) {
            stream.seek(2);
//            System.out.println(stream.readReversedUnsignedShort());
        }
        stream.seek(-2);

        // Read end of table
        int asmPointer = stream.readReversedUnsignedShort();
        int dataTablePointer = stream.readReversedUnsignedShort() | (bank << 16);
        int vramPlacement = stream.readReversedUnsignedShort();

        List<TileFrame> animationFrames = new ArrayList<>();
        for (int i = 0; i < numberOfFrames; i++) {
            TileFrame tileFrame = new TileFrame(index);
            stream.setPosition(BitHelper.snesToPc(tableStart));
            stream.seek(i * 4);
            tileFrame.load(stream);
            animationFrames.add(tileFrame);
        }

        this.animationFrames = new ArrayList<>();
        this.animationFrames.addAll(animationFrames);
    }

    @Override
    public void save(ByteStream stream) {

    }
}
