package com.ostsoft.games.jsm;

public enum Pointer {
    // Samus
    SamusAnimationSpeedBaseTablePointer(0x91B010),
    DMAFrameBaseTablePointer(0x92D94E),
    DMATopTablePointer(0x92D91E),
    DMABottomTablePointer(0x92D938),
    BaseTopTablePointerPointer(0x929263),
    BaseBottomTablePointerPointer(0x92945D),
    BasePoseTablePointer(0x92808D),

    // Misc
    SavePointPointerTable(0x80C4B5),
    HUDTiles(0x8ED5FF), // length: 1280 bytes 2bpp tiles

    PauseTiles(0xB68000),
    MapPauseTileMap(0xB6E000),
    SamusPauseTileMap(0xB6E800),

    // Layer3
    Layer3BaseTablePointer(0x1ABF0),
    Layer3Tiles(0xD3200),

    // Credits
    CreditsTiles(0x988304),
    CreditsTileMaps(0x97EEFF),
    CreditsMaps(0x8CD91B),

    // Logo
    TitleScreenTiles(0x9580D8),
    SuperMetroidSpriteMaps(0x8C879D),
    BetaMetroidLogoSpriteMaps(0x8C8000),
    NintendoLogoSpriteMaps(0x8C80BB),
    Nintendo1994SpriteMaps(0x8C8103),
    RD1SpriteMaps(0x8C8137),
    ProjectSamus(0x8C81CF),
    NintendoPresentsSpriteMaps(0x8C82FF),
    Metroid3UnusedSpriteMaps(0x8C82AD),
    SuperMetroidUnusedSpriteMaps(0x8C833D),
    MetrSpriteMaps(0x8C85C8),
    MetroSpriteMaps(0x8C85F2),
    MetroiSpriteMaps(0x8C867D),
    MetroidSpriteMaps(0x8C86BB),
    Metroid_SpriteMaps(0x8C8703),
    Metroid3SpriteMaps(0x8C874B),
//    LagreEndingSamus1SpriteMaps(0x8C99E4),

    GalaxyIsAtPeace(0x978d12),
    Intro1Tiles(0x95E4C2),
    Intro2Tiles(0x95F90E),
    SpaceColonyTiles(0x96D10A),
    ZebesTiles(0x96EC76),
    LagreSamusTiles(0x979803),
    LagreNoSuitSamusTiles(0x97B957),
    LagreSamus2Tiles(0x97D7FC),
    Font3(0x97E7DE),


    IntroMotherBrainSpriteMaps(0x8C8C00),
    AstroidBelt1SpriteMaps(0x8C909D),
    AstroidBelt2SpriteMaps(0x8C90FE),
    SpaceColonySpriteMaps(0x8C9150),

//    ZebesSpriteMaps(0x8C9566),

    // Intro
    Intro2Tilemap(0x96FF14), // This is compressed, uses Intro2Tiles tiles


    EOL(0);


    public final int pointer;

    Pointer(int pointer) {
        this.pointer = pointer;
    }
}
