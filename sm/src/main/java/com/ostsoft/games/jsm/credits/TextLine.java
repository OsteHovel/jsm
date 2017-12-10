package com.ostsoft.games.jsm.credits;

import com.ostsoft.games.jsm.common.DnD;
import com.ostsoft.games.jsm.common.TileMap;

import java.awt.*;

public class TextLine extends CreditLine implements DnD {
    private CreditsTileMap tileMap;

    public TextLine(CreditsTileMap tileMap) {
        this.tileMap = tileMap;
    }

    @Override
    public Image getImage() {
        return tileMap.getImage();
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone() {
        return new TextLine(tileMap);
    }
}
