package com.ostsoft.games.jsm.editor.tilemap;

import com.ostsoft.games.jsm.common.AttTile;
import com.ostsoft.games.jsm.credits.CreditsTile;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.table.EditorTable;
import com.ostsoft.games.jsm.editor.common.table.columns.BooleanMethodRow;
import com.ostsoft.games.jsm.editor.common.table.columns.IntegerMethodRow;
import com.ostsoft.games.jsm.editor.common.table.columns.StaticStringRow;

public class TileMapTable extends EditorTable implements Observer {
    private final EditorData editorData;
    private final TileMapPanelData tileMapPanelData;

    public TileMapTable(final EditorData editorData, final TileMapPanelData tileMapPanelData) {
        super(editorData);
        this.editorData = editorData;
        this.tileMapPanelData = tileMapPanelData;
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (cellEditor != null) {
            cellEditor.cancelCellEditing();
        }

        if (eventType == EventType.TILEMAP_SELECTED_TILE) {
            if (tileMapPanelData.getTile() != null) {
                model.clear();
                model.add(new StaticStringRow(editorData, new String[]{"Type"}, tileMapPanelData.getTile().getClass().getSimpleName()));
                if (tileMapPanelData.getTile() instanceof CreditsTile) {
                    CreditsTile creditsTile = (CreditsTile) tileMapPanelData.getTile();
                    model.add(new IntegerMethodRow(editorData, new String[]{"PaletteIndex"}, creditsTile, "getPaletteIndex", "setPaletteIndex", EventType.TILE_UPDATED));
                }
                else if (tileMapPanelData.getTile() instanceof AttTile) {
                    AttTile attTile = (AttTile) tileMapPanelData.getTile();
                    model.add(new IntegerMethodRow(editorData, new String[]{"PaletteIndex"}, attTile, "getPaletteIndex", "setPaletteIndex", EventType.TILE_UPDATED));
                    model.add(new BooleanMethodRow(editorData, new String[]{"Flip X"}, attTile, "isFlipX", "setFlipX", EventType.TILE_UPDATED));
                    model.add(new BooleanMethodRow(editorData, new String[]{"Flip Y"}, attTile, "isFlipY", "setFlipY", EventType.TILE_UPDATED));
                }

            }
            else {
                model.clear();
            }
        }

        model.update();
    }
}
