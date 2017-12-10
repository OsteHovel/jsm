package com.ostsoft.games.jsm.editor.common.observer;

public enum EventType {
    // System
    UNDO_REDO_CHANGED,
    STATUS_BAR_MESSAGE,
    CLEAR_UNDO_REDO, // Clears the Undo/Redo queue

    // General
    LOAD, // Load new rom
    SAVE, // Save rom
    SAVE_AS, // Save rom as new fileName
    CLOSE, // Close current rom
    EXIT, // Close editor

    // Animation
    SPRITE_SELECTED,
    SPRITE_UPDATED,
    SPRITE_MOVED,
    ANIMATION,
    ANIMATION_UPDATED,
    ANIMATION_FRAME,
    ANIMATION_FRAME_UPDATED,

    // Credits
    CREDITSLINE_SELECTED,
    CREDITSLINE_UPDATED,
    CREDITSLINES_UPDATED,

    // TileMap
    TILEMAP_SELECTED,
    TILEMAP_UPDATED,
    TILEMAP_SELECTED_TILE,

    // Palette
    PALETTES_UPDATED,
    PALETTE_SELECTED,
    PALETTE_COLOR_SELECTED,
    PALETTE_UPDATED,

    TILE_UPDATED,
    VARIABLE_UPDATE,
    OPTION_UPDATED,


}
