package com.ostsoft.games.jsm.common;

import com.ostsoft.games.jsm.util.ByteStream;

public interface Storable {
    void load(ByteStream stream);

    void save(ByteStream stream);

    int getOffset();

    void setOffset(int offset);

    int getSize();
}
