package com.ostsoft.games.jsm.common;

import java.util.List;

public abstract class TileMap {
    private List<Tile> tiles;

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }
}
