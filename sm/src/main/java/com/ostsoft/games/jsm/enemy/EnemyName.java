package com.ostsoft.games.jsm.enemy;

import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class EnemyName implements Storable {
    public String name;
    private int offset;

    public EnemyName(int offset) {
        this.offset = offset;
    }

    @Override
    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        name = stream.readString(10);
    }

    @Override
    public void save(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        stream.writeString(name);
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
        return 10;
    }
}
