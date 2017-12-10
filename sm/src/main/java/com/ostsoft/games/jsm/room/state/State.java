package com.ostsoft.games.jsm.room.state;

import com.ostsoft.games.jsm.util.ByteStream;

import java.util.List;

public class State {
    public int mapPointer; // Pointer 3-bytes
    public int graphicSet;
    public int musicTrack;
    public int musicControl;
    public int fx1Pointer; // Pointer 2-bytes
    public int enemyLocationPointer; // Pointer 2-bytes
    public int enemySetPointer; // Pointer 2-bytes
    public int scrollLayer2; // 2-bytes, this is how background scrolls
    public int scrollPointer; // Pointer 2-bytes
    public int xrayPointer; // Pointer 2-bytes
    public int mainASMPointer; // Pointer 2-bytes
    public int plmPointer; // Pointer 2-bytes
    public int backgroundPointer; // Pointer 2-bytes
    public int setupASMPointer; // 2-bytes


    public int[] scroll;
    public Layer1And2 layer1And2;
    public Background background;
    public FX1 fx1;
    public List<EnemyLocation> enemyLocations;
    public int enemiesToKillBeforeRoomIsClearedOut;
    public List<PLM> plms;
    public List<ScrollPLM> scrollPLMs;


    public State(ByteStream stream) {
        mapPointer = stream.readReversedUnsigned3Bytes();
        graphicSet = stream.readUnsignedByte();
        musicTrack = stream.readUnsignedByte();
        musicControl = stream.readUnsignedByte();
        fx1Pointer = stream.readReversedUnsignedShort() | (0x83 << 16);
        enemyLocationPointer = stream.readReversedUnsignedShort() | (0xA1 << 16);
        enemySetPointer = stream.readReversedUnsignedShort() | (0xB4 << 16);
        scrollLayer2 = stream.readReversedUnsignedShort();
        scrollPointer = stream.readReversedUnsignedShort() | (0x8F << 16);
        xrayPointer = stream.readReversedUnsignedShort() | (0x8F << 16);
        mainASMPointer = stream.readReversedUnsignedShort() | (0x84 << 16);
        plmPointer = stream.readReversedUnsignedShort() | (0x8F << 16);
        backgroundPointer = stream.readReversedUnsignedShort() | (0x8F << 16);
        setupASMPointer = stream.readReversedUnsignedShort() | (0x8F << 16);
    }

    public void printDebug() {
        System.out.println("Map: " + Integer.toHexString(mapPointer));
        System.out.println("GraphicSet: " + graphicSet);
        System.out.println("MusicTrack: " + musicTrack);
        System.out.println("MusicControl: " + musicControl);

        if (enemyLocations != null) {
            System.out.println("Number of enemies: " + enemyLocations.size());
        }
        System.out.println("Number of enemies to kill before clearout: " + enemiesToKillBeforeRoomIsClearedOut);

    }

    public boolean isLayer2Background() {
        return (scrollLayer2 & 0x1) != 0x1;
    }

    public int getXScroll() {
        return (scrollLayer2 >> 12) & 0xF;
    }

    public int getYScroll() {
        return (scrollLayer2 >> 4) & 0xF;
    }
}
