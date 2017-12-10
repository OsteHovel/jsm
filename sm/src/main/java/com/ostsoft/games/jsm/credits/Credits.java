package com.ostsoft.games.jsm.credits;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.common.SimpleTile;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.common.TileMap;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import java.util.ArrayList;
import java.util.List;

public class Credits {
    private List<Tile> tiles;
    private List<TileMap> tileMaps;
    private List<CreditLine> creditsMap = new ArrayList<>();

    public Credits load(ByteStream stream, Palette palette) {

        byte[] tmpTiles = CompressUtil.decompress(stream, BitHelper.snesToPc(Pointer.CreditsTiles.pointer));
        int len = 512 * 16;
        byte[] tilesBytes = new byte[tmpTiles.length - len];
        System.arraycopy(tmpTiles, len, tilesBytes, 0, tmpTiles.length - len);

        tiles = new ArrayList<>();
        for (int i = 0; i < tilesBytes.length / 32; i++) {
            byte[] tileBytes = new byte[32];
            System.arraycopy(tilesBytes, i * 32, tileBytes, 0, tileBytes.length);
            tiles.add(new SimpleTile(tileBytes));
        }

        byte[] tileMapBytes = CompressUtil.decompress(stream, BitHelper.snesToPc(Pointer.CreditsTileMaps.pointer));
        tileMaps = new ArrayList<>();
        for (int i = 0; i < tileMapBytes.length; i += 64) {
            tileMaps.add(new CreditsTileMap(tiles, tileMapBytes, palette, i));
        }


        stream.setPosition(BitHelper.snesToPc(Pointer.CreditsMaps.pointer));
        while (true) {
            int b1 = stream.readUnsignedByte();
            int b2 = stream.readUnsignedByte();
            if (b1 == 0x17 && b2 == 0x9A) { // Blank line
                int repeat = stream.readReversedUnsignedShort();
                stream.seek(4);
                creditsMap.add(new BlankLine(repeat));
            }
            else if (b1 == 0x0D && b2 == 0x9A) { // Text line
                int pointerToLastBlankLine = stream.readReversedUnsignedShort();
            }
            else if (b1 == 0x00 && b2 == 0x00) { // Text line inline
                int tileIndex = stream.readReversedUnsignedShort();
                creditsMap.add(new TextLine((CreditsTileMap) tileMaps.get(tileIndex / 64)));
            }
            else {
//                System.out.println("The end " + Integer.toHexString(b1).toUpperCase() + " " + Integer.toHexString(b2).toUpperCase());
//                for (int i = 0; i < 10; i++) {
//                    System.out.print(Integer.toHexString(stream.readUnsignedByte()).toUpperCase() + " ");
//                }
                break;
            }
        }


        return this;
    }

    public Credits save(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(Pointer.CreditsMaps.pointer));
        CreditLine lastLine = null;
        for (CreditLine creditLine : creditsMap) {
            if (creditLine instanceof TextLine) {
                if (!(lastLine instanceof TextLine)) {
                    stream.writeUnsignedByte(0x0D);
                    stream.writeUnsignedByte(0x9A);
                    int pointerPos = BitHelper.pcToSnes(stream.getPosition());
                    pointerPos -= 6;
                    stream.writeUnsignedReversedShort(pointerPos);
                }
                stream.writeUnsignedByte(0x00);
                stream.writeUnsignedByte(0x00);
                stream.writeUnsignedReversedShort(tileMaps.indexOf(((TextLine) creditLine).getTileMap()) * 64);
            }
            else if (creditLine instanceof BlankLine) {
                BlankLine blankLine = (BlankLine) creditLine;
                blankLine.numberOfLines = Math.max(Math.min(blankLine.numberOfLines, 0xFFFF), 1);

                stream.writeUnsignedByte(0x17);
                stream.writeUnsignedByte(0x9A);

                stream.writeUnsignedReversedShort(blankLine.numberOfLines);
                stream.writeUnsignedByte(0x00);
                stream.writeUnsignedByte(0x00);

                stream.writeUnsignedByte(0xC0);
                stream.writeUnsignedByte(0x1F);
//                stream.writeUnsignedReversedShort(0x1FC0);
            }
            lastLine = creditLine;
        }

        stream.writeUnsignedByte(0x0D);
        stream.writeUnsignedByte(0x9A);

        int pointerPos = BitHelper.pcToSnes(stream.getPosition());
        pointerPos -= 6;
        stream.writeUnsignedReversedShort(pointerPos);

        stream.writeUnsignedByte(0xFE);
        stream.writeUnsignedByte(0xF6);
        stream.writeUnsignedByte(0xFE);
        stream.writeUnsignedByte(0x99);

        // Need to save tileMaps too.


        return this;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<CreditLine> getCreditsMap() {
        return creditsMap;
    }

    public List<TileMap> getTileMaps() {
        return tileMaps;
    }

}
