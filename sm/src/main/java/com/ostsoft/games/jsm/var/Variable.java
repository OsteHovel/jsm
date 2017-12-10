package com.ostsoft.games.jsm.var;

import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class Variable {
    private final String name;
    private final Type type;
    private int offset;
    private int value;

    public Variable(String name, Type type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
    }

    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        switch (type) {
            case BYTE:
                value = stream.readUnsignedByte();
                break;
            case SHORT:
                value = stream.readReversedUnsignedShort();
                break;
        }
    }

    public void save(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        switch (type) {
            case BYTE:
                stream.writeUnsignedByte(value);
                break;
            case SHORT:
                stream.writeUnsignedReversedShort(value);
                break;
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
