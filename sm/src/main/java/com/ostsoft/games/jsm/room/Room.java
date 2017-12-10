package com.ostsoft.games.jsm.room;

import com.ostsoft.games.jsm.room.state.Background;
import com.ostsoft.games.jsm.room.state.EnemyLocation;
import com.ostsoft.games.jsm.room.state.FX1;
import com.ostsoft.games.jsm.room.state.Layer1And2;
import com.ostsoft.games.jsm.room.state.PLM;
import com.ostsoft.games.jsm.room.state.ScrollPLM;
import com.ostsoft.games.jsm.room.state.State;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;
import com.ostsoft.games.jsm.util.PLMUtil;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int offset;
    private Header header;
    private List<StateSelect> stateSelects;
    private List<State> states;
    private List<Door> doors;

    public Room(ByteStream stream, int offset) {
        this.offset = offset;
        stream.setPosition(offset);
        header = new Header(stream);

        stateSelects = new ArrayList<>();
        StateSelect stateSelect = new StateSelect(stream);
        stateSelects.add(stateSelect);
        while (stateSelect.type != 0xE5E6) {
            stateSelect = new StateSelect(stream);
            stateSelects.add(stateSelect);
        }

        states = new ArrayList<>();
        for (StateSelect stateSelect1 : stateSelects) {
            State state = new State(stream);
            states.add(state);
        }

        // Now its resetting stream
        for (State state : states) {
            byte[] decompressLayer1And2 = CompressUtil.decompress(stream, BitHelper.snesToPc(state.mapPointer));
            state.layer1And2 = new Layer1And2(decompressLayer1And2);

            int backgroundOffset = BitHelper.snesToPc(state.backgroundPointer);
            stream.setPosition(backgroundOffset);
            state.background = new Background(stream, backgroundOffset);


            stream.setPosition(BitHelper.snesToPc(state.fx1Pointer));
            state.fx1 = new FX1(stream);

            state.enemyLocations = new ArrayList<>();
            stream.setPosition(BitHelper.snesToPc(state.enemyLocationPointer));
            EnemyLocation enemyLocation = new EnemyLocation(stream);
            while (!((enemyLocation.enemyDataPointer & 0xFFFF) == 0xFFFF)) {
                state.enemyLocations.add(enemyLocation);
                enemyLocation = new EnemyLocation(stream);
            }
            state.enemiesToKillBeforeRoomIsClearedOut = enemyLocation.x & 0xFF;


            // PLM
            state.plms = new ArrayList<>();
            stream.setPosition(BitHelper.snesToPc(state.plmPointer));
            PLM plm = new PLM(stream);
            while (plm.command != 0x0000) {
                state.plms.add(plm);
                plm = new PLM(stream);
            }

            state.scrollPLMs = new ArrayList<>();
            for (PLM plm2 : state.plms) {
                if (PLMUtil.PLMType.getPLMType(plm2.command) == PLMUtil.PLMType.SCROLL) {
                    int scrollPointer = plm2.arguments | (0x8F << 16);
                    stream.setPosition(BitHelper.snesToPc(scrollPointer));

                    ScrollPLM scrollPLM = new ScrollPLM(stream);
                    while (scrollPLM.screen != 0x80) {
                        state.scrollPLMs.add(scrollPLM);
                        scrollPLM = new ScrollPLM(stream);
                    }
                }
            }

            // Scroll
            stream.setPosition(BitHelper.snesToPc(state.scrollPointer));
            state.scroll = new int[header.width * header.height];
            for (int i = 0; i < header.width * header.height; i++) {
                state.scroll[i] = stream.readUnsignedByte();
            }
        }

        stream.setPosition(BitHelper.snesToPc(header.doorOut));
        List<Integer> doorOffsets = new ArrayList<>();
        while (true) {
            int doorOffset = stream.readReversedUnsignedShort() | (0x83 << 16);
            if ((doorOffset & 0xFFFF) < 0x8000) {
                break;
            }
            doorOffsets.add(doorOffset);
        }

        doors = new ArrayList<>();
        for (Integer doorOffset : doorOffsets) {
            stream.setPosition(BitHelper.snesToPc(doorOffset));
            doors.add(new Door(stream));
        }

    }


    public void printDebug() {
        System.out.println("Offset: " + Integer.toHexString(offset));
        header.printDebug();

        for (StateSelect stateSelect : stateSelects) {
            stateSelect.printDebug();
        }
        for (State state : states) {
            state.printDebug();
        }
    }

    public Header getHeader() {
        return header;
    }

    public List<StateSelect> getStateSelects() {
        return stateSelects;
    }

    public List<State> getStates() {
        return states;
    }

    public int getOffset() {
        return offset;
    }

    public List<Door> getDoors() {
        return doors;
    }
}
