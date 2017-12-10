package com.ostsoft.games.jsm.editor.tilemap;

import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.common.TileMap;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PaletteData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.palette.Palette;

import java.util.List;

public class TileMapPanelData extends PanelData implements PaletteData {
    private Class tileType;
    private List<TileMap> tileMaps;
    private TileMap tileMap;

    private List<Tile> tiles;
    private Tile tile;
    private Palette palette;

    public TileMapPanelData(EditorData editorData) {
        super(editorData);
    }

    public Class getTileType() {
        return tileType;
    }

    public void setTileType(Class tileType) {
        this.tileType = tileType;
    }

    public List<TileMap> getTileMaps() {
        return tileMaps;
    }

    public void setTileMaps(List<TileMap> tileMaps) {
        this.tileMaps = tileMaps;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public Palette getPalette() {
        return palette;
    }

    @Override
    public void setPalette(Palette palette) {
        this.palette = palette;
    }
}
