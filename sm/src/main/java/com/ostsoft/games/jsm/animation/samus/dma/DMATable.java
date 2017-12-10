package com.ostsoft.games.jsm.animation.samus.dma;

import com.ostsoft.games.jsm.animation.TileManager;
import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.List;

public class DMATable implements Storable {
    private final TileManager tileManager;
    private int offset;
    private List<DMAEntry> entryList;

    public DMATable(TileManager tileManager, int offset) {
        this.tileManager = tileManager;
        this.offset = offset;
    }

    @Override
    public void load(ByteStream stream) {
        entryList = new ArrayList<>();
        for (int i = 0; i <= 0x1F; i++) {
            int offset = BitHelper.pcToSnes(BitHelper.snesToPc(this.offset) + (7 * i));
            DMAEntry dmaEntry = new DMAEntry(tileManager, offset);
            stream.setPosition(BitHelper.snesToPc(offset));
            stream.seek(2);
            if (stream.readUnsignedByte() >= 0xFC) {
                break;
            }
            dmaEntry.load(stream);
            entryList.add(dmaEntry);
        }
    }

    @Override
    public void save(ByteStream stream) {
        for (DMAEntry dmaEntry : entryList) {
            dmaEntry.save(stream);
        }
    }

    public DMAEntry get(int index) {
        return entryList.get(index);
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
        int size = 0;
        for (DMAEntry dmaEntry : entryList) {
            size += dmaEntry.getSize();
        }
        return size;
    }

    public void printDebug() {
        int index = 0;
        for (DMAEntry dmaEntry : entryList) {
            System.out.print("DMA[" + index + "]: ");
            dmaEntry.printDebug();
            index++;
        }
    }

    public List<DMAEntry> getEntryList() {
        return entryList;
    }
}
