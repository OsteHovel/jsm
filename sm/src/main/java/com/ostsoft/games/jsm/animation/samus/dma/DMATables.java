package com.ostsoft.games.jsm.animation.samus.dma;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.animation.TileManager;
import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class DMATables implements Storable {
    private TileManager tileManager = new TileManager();

    private List<DMATable> topTables;
    private List<DMATable> bottomTables;

    @Override
    public void load(ByteStream stream) {
        // Read out pointers to top table
        topTables = new ArrayList<>();
        stream.setPosition(BitHelper.snesToPc(Pointer.DMATopTablePointer.pointer));
        for (int i = 0; i <= 0x0C; i++) {
            topTables.add(new DMATable(tileManager, stream.readReversedUnsignedShort() | (0x92 << 16)));
        }

        // Read out pointers to bottom table
        bottomTables = new ArrayList<>();
        stream.setPosition(BitHelper.snesToPc(Pointer.DMABottomTablePointer.pointer));
        for (int i = 0; i <= 0x0A; i++) {
            bottomTables.add(new DMATable(tileManager, stream.readReversedUnsignedShort() | (0x92 << 16)));
        }

        // Read out top tables
        for (DMATable dmaTable : topTables) {
            dmaTable.load(stream);
        }


        // Read out bottom tables
        for (DMATable dmaTable : bottomTables) {
            dmaTable.load(stream);
        }
    }

    @Override
    public void save(ByteStream stream) {
        for (DMATable dmaTable : topTables) {
            dmaTable.save(stream);
        }

        for (DMATable dmaTable : bottomTables) {
            dmaTable.save(stream);
        }
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
        int size = 0;
        for (DMATable dmaTable : topTables) {
            size += dmaTable.getSize();
        }

        for (DMATable dmaTable : bottomTables) {
            size += dmaTable.getSize();
        }
        return size;
    }

    public List<DMATable> getTopTables() {
        return topTables;
    }

    public List<DMATable> getBottomTables() {
        return bottomTables;
    }
}
