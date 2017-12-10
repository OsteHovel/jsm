package com.ostsoft.games.jsm.animation.samus;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.FrameManager;
import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.animation.SpriteMapManager;
import com.ostsoft.games.jsm.animation.samus.dma.DMAEntry;
import com.ostsoft.games.jsm.animation.samus.dma.DMAFrame;
import com.ostsoft.games.jsm.animation.samus.dma.DMATables;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SamusFrame extends AnimationFrame {
    private final DMATables dmaTables;
    private final SpriteMapManager spriteMapManager;
    public int poseIndex;
    public int frameIndex;
    public DMAFrame dmaFrame;
    private List<SpriteMapEntry> topSprites;
    private List<SpriteMapEntry> bottomSprites;

    public SamusFrame(DMATables dmaTables, SpriteMapManager spriteMapManager, FrameManager frameManager, int poseIndex, int frameIndex) {
        this.dmaTables = dmaTables;
        this.spriteMapManager = spriteMapManager;
        this.poseIndex = poseIndex;
        this.frameIndex = frameIndex;
    }

    public void load(ByteStream stream) {
        // Reading frame length
        stream.setPosition(BitHelper.snesToPc(SamusAnimation.getAnimationFrameSpeedPointer(stream, poseIndex)));
        stream.seek(frameIndex);
        this.setFrameLength(stream.readUnsignedByte());

        // Reading DMAFrame
        int dmaFrameTablePointer = getDmaFrameTablePointer(stream, poseIndex);
        stream.setPosition(BitHelper.snesToPc(dmaFrameTablePointer));
        stream.seek(4 * frameIndex); // 4 bytes per DMAFrame
        dmaFrame = new DMAFrame(stream);

        loadTiles(dmaTables);

        // Reading spriteMapTable
        int poseTopTP = getPointerToPoseTable(stream, poseIndex, Pointer.BaseTopTablePointerPointer.pointer);
        stream.setPosition(BitHelper.snesToPc(poseTopTP));
        stream.seek(frameIndex * 2);
        int topPointer = stream.readReversedUnsignedShort() | 0x92 << 16;

        int poseBottomTP = getPointerToPoseTable(stream, poseIndex, Pointer.BaseBottomTablePointerPointer.pointer);
        stream.setPosition(BitHelper.snesToPc(poseBottomTP));
        stream.seek(frameIndex * 2);
        int bottomPointer = stream.readReversedUnsignedShort() | 0x92 << 16;

        // Read spriteMapEntries
        stream.setPosition(BitHelper.snesToPc(topPointer));
        topSprites = readSpriteEntries(spriteMapManager, stream);

        stream.setPosition(BitHelper.snesToPc(bottomPointer));
        int lengthBottom = stream.readReversedUnsignedShort();
        if (lengthBottom < 35592) {
            stream.seek(-2);
            bottomSprites = readSpriteEntries(spriteMapManager, stream);
        }
        else {
            bottomSprites = Collections.emptyList();
        }
    }

    private List<SpriteMapEntry> readSpriteEntries(SpriteMapManager spriteMapManager, ByteStream stream) {
        int length = stream.readReversedUnsignedShort();
        List<SpriteMapEntry> spriteMapEntries = new ArrayList<>(length);
        for (int j = 0; j < length; j++) {
            int position = BitHelper.pcToSnes(stream.getPosition());
            SpriteMapEntry spriteMapEntry = spriteMapManager.getSpriteMapEntry(position);
            if (spriteMapEntry == null) {
                spriteMapEntry = new SpriteMapEntry();
                spriteMapEntry.load(stream);
                spriteMapManager.putSpriteMapEntry(position, spriteMapEntry);
            }
            else {
                stream.seek(5);
            }
            spriteMapEntries.add(spriteMapEntry);
        }
        return spriteMapEntries;
    }

    public void save(ByteStream stream) {
        // Writing frame length
        stream.setPosition(BitHelper.snesToPc(SamusAnimation.getAnimationFrameSpeedPointer(stream, poseIndex)));
        stream.seek(frameIndex);
        setFrameLength(frameLength);
        stream.writeUnsignedByte(getFrameLength());

        // Writing DMAFrame
        int dmaFrameTablePointer = getDmaFrameTablePointer(stream, poseIndex);
        stream.setPosition(BitHelper.snesToPc(dmaFrameTablePointer));
        stream.seek(4 * frameIndex); // 4 bytes per DMAFrame
        dmaFrame.save(stream);

        // Reading spriteMapTable
        int poseTopTP = getPointerToPoseTable(stream, poseIndex, Pointer.BaseTopTablePointerPointer.pointer);
        stream.setPosition(BitHelper.snesToPc(poseTopTP));
        stream.seek(frameIndex * 2);
        int topPointer = stream.readReversedUnsignedShort() | 0x92 << 16;

        int poseBottomTP = getPointerToPoseTable(stream, poseIndex, Pointer.BaseBottomTablePointerPointer.pointer);
        stream.setPosition(BitHelper.snesToPc(poseBottomTP));
        stream.seek(frameIndex * 2);
        int bottomPointer = stream.readReversedUnsignedShort() | 0x92 << 16;

        // Writing spriteMapEntries
        stream.setPosition(BitHelper.snesToPc(topPointer));
        stream.writeUnsignedReversedShort(topSprites.size());
        for (SpriteMapEntry topSprite : topSprites) {
            topSprite.save(stream);
        }

        if (bottomSprites != null && bottomSprites.size() > 0) {
            stream.setPosition(BitHelper.snesToPc(bottomPointer));
            stream.writeUnsignedReversedShort(bottomSprites.size());
            for (SpriteMapEntry bottomSprite : bottomSprites) {
                bottomSprite.save(stream);
            }
        }
    }

    public void loadTiles(DMATables dmaTables) {
        DMAEntry topEntry = dmaTables.getTopTables().get(dmaFrame.indexTopHalfTable).get(dmaFrame.indexTopHalfEntry);
        DMAEntry bottomEntry = dmaTables.getBottomTables().get(dmaFrame.indexBottomHalfTable).get(dmaFrame.indexBottomHalfEntry);

        List<Tile> tiles = getTiles();
        tiles.clear();

        tiles.addAll(topEntry.getTilesPart1());
        tiles.addAll(bottomEntry.getTilesPart1());
        tiles.addAll(topEntry.getTilesPart2());
        tiles.addAll(bottomEntry.getTilesPart2());
    }

    private int getDmaFrameTablePointer(ByteStream stream, int poseIndex) {
        // Reading dmaFrame pointer
        stream.setPosition(BitHelper.snesToPc(Pointer.DMAFrameBaseTablePointer.pointer + poseIndex * 2));
        return stream.readReversedUnsignedShort() | (0x92 << 16);
    }

    protected int getPointerToPoseTable(ByteStream byteStream, int poseIndex, int baseTablePointerPointer) {
        int poseTablePointerPointer2 = baseTablePointerPointer + poseIndex * 2;
        byteStream.setPosition(BitHelper.snesToPc(poseTablePointerPointer2));

        int poseTableOffset = byteStream.readReversedUnsignedShort();
        int calculatedPoseTableOffset = poseTableOffset * 2;
        return Pointer.BasePoseTablePointer.pointer + calculatedPoseTableOffset;
    }

    public List<SpriteMapEntry> getTopSprites() {
        return topSprites;
    }

    public List<SpriteMapEntry> getBottomSprites() {
        return bottomSprites;
    }

    @Override
    public List<SpriteMapEntry> getSpriteMapEntries() {
        return Stream.concat(topSprites.stream(), bottomSprites.stream()).collect(Collectors.toList());
    }

    @Override
    public void setSpriteMapEntries(List<SpriteMapEntry> spriteMapEntries) {
        System.out.println("[SamusFrame] setSpriteMapEntries() is not supported");
    }
}
