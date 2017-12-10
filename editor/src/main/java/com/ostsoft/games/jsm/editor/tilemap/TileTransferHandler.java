package com.ostsoft.games.jsm.editor.tilemap;

import com.ostsoft.games.jsm.common.AttTile;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.credits.CreditsTile;
import com.ostsoft.games.jsm.editor.common.list.EventListItemTransferHandler;
import com.ostsoft.games.jsm.editor.common.list.EventListModel;

public class TileTransferHandler<T> extends EventListItemTransferHandler<T> {
    private final Class tileType;
    private final TileMapPanelData tileMapPanelData;

    public TileTransferHandler(int sourceAction, Class tileType, TileMapPanelData tileMapPanelData) {
        super(sourceAction);
        this.tileType = tileType;
        this.tileMapPanelData = tileMapPanelData;
    }

    @Override
    protected void addToList(EventListModel listModel, Object value, int idx) {

        if (tileType.isInstance(value)) {
            super.addToList(listModel, value, idx);
        }
        else if (value instanceof Tile) {
            Tile tile = (Tile) value;
            if (tileType == CreditsTile.class) {
                if (tileMapPanelData.getPalette() != null) {
                    super.addToList(listModel, new CreditsTile(tile, tileMapPanelData.getPalette()), idx);
                }
                else {
                    System.out.println("You need to select a palette first...");
                }
            }
            else if (tileType == AttTile.class) {
                super.addToList(listModel, new AttTile(tile, 4, 0, false, false), idx);
            }
            else {
                System.out.println("Unsuported tileType!");
            }
        }
        else {
            System.out.println("Not a TILE TYPE!");
        }
    }
}
