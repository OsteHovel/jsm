package com.ostsoft.games.jsm;

import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class SaveStation implements Storable {
    private int area = 0;

    @Override
    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(Pointer.SavePointPointerTable.pointer) + area * 2);
        int offset = stream.readReversedUnsignedShort() | 0x80 << 16;
        stream.setPosition(BitHelper.snesToPc(offset));


    }

    @Override
    public void save(ByteStream stream) {

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
        return 0;
    }
}
