package com.ostsoft.games.jsm;

import com.ostsoft.games.jsm.animation.FrameManager;
import com.ostsoft.games.jsm.animation.Logo;
import com.ostsoft.games.jsm.animation.SpriteMapManager;
import com.ostsoft.games.jsm.animation.samus.SamusAnimation;
import com.ostsoft.games.jsm.animation.samus.dma.DMATables;
import com.ostsoft.games.jsm.animation.samus.pose.Pose;
import com.ostsoft.games.jsm.animation.tile.TileEntry;
import com.ostsoft.games.jsm.credits.Credits;
import com.ostsoft.games.jsm.enemy.Enemy;
import com.ostsoft.games.jsm.enemy.EnemyName;
import com.ostsoft.games.jsm.graphics.GraphicSet;
import com.ostsoft.games.jsm.layer3.Layer3;
import com.ostsoft.games.jsm.layer3.Layer3Type;
import com.ostsoft.games.jsm.palette.PaletteEnum;
import com.ostsoft.games.jsm.palette.PaletteManager;
import com.ostsoft.games.jsm.projectile.Projectile;
import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.jsm.room.state.EnemyLocation;
import com.ostsoft.games.jsm.room.state.State;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.var.Physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperMetroid {
    private Header header;
    private List<Room> rooms;
    private Map<Integer, Enemy> enemies;
    private List<GraphicSet> graphicSets;
    private PaletteManager paletteManager;
    private Layer3[] layer3List;
    private List<Projectile> projectiles;
    private DMATables dmaTables;
    private List<SamusAnimation> samusAnimations;
    private Credits credits;
    private List<TileEntry> tileEntries;
    private List<Logo> logos;
    private Physics physics;
    private List<Pose> poses;

    private SpriteMapManager spriteMapManager = new SpriteMapManager();
    private FrameManager frameManager = new FrameManager();


    private ByteStream stream;
    private int loadedFlags;

    public SuperMetroid(ByteStream stream, int flags) {
        this(stream, flags, Collections.emptyList(), Collections.emptyList());
    }

    public SuperMetroid(ByteStream stream, int flags, List<Integer> roomList, List<Integer> enemyPointerList) {
        this.stream = stream;
        load(flags, roomList, enemyPointerList);
    }

    public void load(int flags) {
        load(flags, Collections.emptyList(), Collections.emptyList());
    }

    public void load(int flags, List<Integer> roomList, List<Integer> enemyPointerList) {
        if (header == null) {
            header = new Header();
            header.load(stream);
        }
        if (header.isPAL()) {
            return; // PAL
        }

        if ((Flag.PALETTES.hasFlag(flags) || Flag.CREDITS.hasFlag(flags) || Flag.PROJECTILES.hasFlag(flags)) && paletteManager == null) {
            paletteManager = new PaletteManager(stream);
        }

        if (Flag.ROOMS.hasFlag(flags)) {
            if (rooms == null) {
                rooms = new ArrayList<>();
            }

            for (Integer startOfRoom : roomList) {
                boolean exists = false;
                for (Room room : rooms) {
                    if (room.getOffset() == startOfRoom) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    Room room = new Room(stream, startOfRoom);
                    rooms.add(room);
                }
            }
        }

        if (Flag.GRAPHICSETS.hasFlag(flags) && graphicSets == null) {
            graphicSets = new ArrayList<>();
            for (int i = 0; i < 29; i++) {
                graphicSets.add(new GraphicSet(stream, i));
            }
        }

        if (Flag.ENEMIES.hasFlag(flags) && this.enemies == null) {
            this.enemies = new HashMap<>();

            if (Flag.ROOMS.hasFlag(flags)) {
                for (Room room : rooms) {
                    for (State state : room.getStates()) {
                        for (EnemyLocation enemyLocation : state.enemyLocations) {
                            if (!this.enemies.containsKey(enemyLocation.enemyDataPointer & 0xFFFF)) {
                                Enemy enemy = new Enemy(enemyLocation.enemyDataPointer);
                                enemy.load(stream);
                                enemy.enemyName = new EnemyName(enemy.namePointer);
                                enemy.enemyName.load(stream);
                                this.enemies.put(enemyLocation.enemyDataPointer & 0xFFFF, enemy);
                            }
                        }
                    }
                }
            }

            for (Integer enemyPointer : enemyPointerList) {
                if (!this.enemies.containsKey((enemyPointer & 0xFFFF))) {
                    Enemy enemy = new Enemy(enemyPointer);
                    enemy.load(stream);
                    enemy.enemyName = new EnemyName(enemy.namePointer);
                    enemy.enemyName.load(stream);
                    this.enemies.put(enemyPointer & 0xFFFF, enemy);
                }
            }
        }

        if (Flag.LAYER3.hasFlag(flags) && layer3List == null) {
            layer3List = new Layer3[0x2D];

            for (Layer3Type layer3Type : Layer3Type.values()) {
                stream.setPosition(Pointer.Layer3BaseTablePointer.pointer + layer3Type.value);
                int layer3Pointer = stream.readReversedUnsignedShort() | (0x8A << 16);

                stream.setPosition(BitHelper.snesToPc(layer3Pointer));
                layer3List[layer3Type.value] = new Layer3(stream, layer3Type);
            }
        }

        if (Flag.PROJECTILES.hasFlag(flags) && projectiles == null) {
            projectiles = new ArrayList<>();
            for (Projectile.ProjectileType projectileType : Projectile.ProjectileType.values()) {
                projectiles.add(new Projectile(stream, paletteManager, projectileType));
            }
        }

        if (Flag.SAMUS.hasFlag(flags) && dmaTables == null) {
            dmaTables = new DMATables();
            dmaTables.load(stream);
            samusAnimations = new ArrayList<>();
            for (int pose = 0; pose <= 0xFC; pose++) {
                SamusAnimation samusAnimation = new SamusAnimation(pose, dmaTables, spriteMapManager, frameManager);
                samusAnimation.load(stream);
                samusAnimations.add(samusAnimation);
            }
        }

        if (Flag.POSES.hasFlag(flags) && poses == null) {
            poses = new ArrayList<>();
            for (int pose = 0; pose <= 0xFC; pose++) {
                poses.add(new Pose().read(stream, pose));
            }
        }

        if (Flag.PHYSICS.hasFlag(flags) && physics == null) {
            physics = new Physics();
            physics.load(stream);
        }

        if (Flag.CREDITS.hasFlag(flags) && credits == null) {
            credits = new Credits().load(stream, paletteManager.getPalette(PaletteEnum.CREDITS));
        }

        if (Flag.ANIMATEDTILES.hasFlag(flags) && tileEntries == null) {
            // Read bg tile animation table
            tileEntries = new ArrayList<>();
            for (int index = 0; index < 12; index++) {
                TileEntry tileEntry = new TileEntry(index);
                tileEntry.load(stream);
                tileEntries.add(tileEntry);
            }
        }

        if (Flag.LOGO.hasFlag(flags) && logos == null) {
            logos = new ArrayList<>();
            logos.add(new Logo("Titlescreen", Pointer.TitleScreenTiles.pointer, Pointer.SuperMetroidSpriteMaps.pointer));
            logos.add(new Logo("Beta Metroid logo", Pointer.TitleScreenTiles.pointer, Pointer.BetaMetroidLogoSpriteMaps.pointer));
            logos.add(new Logo("Nintendo", Pointer.TitleScreenTiles.pointer, Pointer.NintendoLogoSpriteMaps.pointer));
            logos.add(new Logo("Nintendo 1994", Pointer.TitleScreenTiles.pointer, Pointer.Nintendo1994SpriteMaps.pointer));
            logos.add(new Logo("1993 R&D1 PRODUCE (unused)", Pointer.TitleScreenTiles.pointer, Pointer.RD1SpriteMaps.pointer));
            logos.add(new Logo("PROJECT SAMUS FEATURED IN (unused)", Pointer.TitleScreenTiles.pointer, Pointer.ProjectSamus.pointer));
            logos.add(new Logo("Metroid 3 (unused?)", Pointer.TitleScreenTiles.pointer, Pointer.Metroid3UnusedSpriteMaps.pointer));
            logos.add(new Logo("Nintendo presents", Pointer.TitleScreenTiles.pointer, Pointer.NintendoPresentsSpriteMaps.pointer));
            logos.add(new Logo("Titlescreen (Unused)", Pointer.TitleScreenTiles.pointer, Pointer.SuperMetroidUnusedSpriteMaps.pointer));
            logos.add(new Logo("Metr", Pointer.TitleScreenTiles.pointer, Pointer.MetrSpriteMaps.pointer));
            logos.add(new Logo("Metro", Pointer.TitleScreenTiles.pointer, Pointer.MetroSpriteMaps.pointer));
            logos.add(new Logo("Metroi", Pointer.TitleScreenTiles.pointer, Pointer.MetroiSpriteMaps.pointer));
            logos.add(new Logo("Metroid", Pointer.TitleScreenTiles.pointer, Pointer.MetroidSpriteMaps.pointer));
            logos.add(new Logo("Metroid_", Pointer.TitleScreenTiles.pointer, Pointer.Metroid_SpriteMaps.pointer));
            logos.add(new Logo("Metroid 3", Pointer.TitleScreenTiles.pointer, Pointer.Metroid3SpriteMaps.pointer));


            logos.add(new Logo("Astroid belt 1", Pointer.SpaceColonyTiles.pointer, Pointer.AstroidBelt1SpriteMaps.pointer));
            logos.add(new Logo("Astroid belt 2", Pointer.SpaceColonyTiles.pointer, Pointer.AstroidBelt2SpriteMaps.pointer));
            logos.add(new Logo("Space Colony", Pointer.SpaceColonyTiles.pointer, Pointer.SpaceColonySpriteMaps.pointer));

//            logos.add(new Logo("Not working - Intro Motherbrain", Pointer.Intro2Tiles.pointer, Pointer.IntroMotherBrainSpriteMaps.pointer));
//            logos.add(new Logo("Not working - Lagre Samus standing", Pointer.Intro2Tiles.pointer, Pointer.IntroMotherBrainSpriteMaps.pointer));
//            logos.add(new Logo("Not wokring - Zebes", Pointer.ZebesTiles.pointer, Pointer.IntroMotherBrainSpriteMaps.pointer));

            for (Logo logo : logos) {
                logo.load(stream);
            }
        }

        this.loadedFlags = this.loadedFlags | flags;
    }

    public void save() {
        if (samusAnimations != null) {
            for (SamusAnimation samusAnimation : samusAnimations) {
                samusAnimation.save(stream);
            }
        }

        if (dmaTables != null) {
            dmaTables.save(stream);
        }

        if (credits != null) {
            credits.save(stream);
        }

        if (logos != null) {
            for (Logo logo : logos) {
                logo.save(stream);
            }
        }

        if (paletteManager != null) {
            paletteManager.save(stream);
        }

        if (physics != null) {
            physics.save(stream);
        }

    }

    public void unload(int flags) {
        if (Flag.PALETTES.hasFlag(flags)) {
            paletteManager = null;
        }

        if (Flag.ROOMS.hasFlag(flags)) {
            rooms = null;
        }

        if (Flag.GRAPHICSETS.hasFlag(flags) && graphicSets == null) {
            graphicSets = null;
        }

        if (Flag.ENEMIES.hasFlag(flags) && this.enemies == null) {
            this.enemies = null;
        }

        if (Flag.LAYER3.hasFlag(flags)) {
            layer3List = null;
        }

        if (Flag.PROJECTILES.hasFlag(flags)) {
            projectiles = null;
        }

        if (Flag.SAMUS.hasFlag(flags)) {
            dmaTables = null;
            samusAnimations = null;
        }

        if (Flag.POSES.hasFlag(flags)) {
            poses = null;
        }

        if (Flag.PHYSICS.hasFlag(flags)) {
            physics = null;
        }

        if (Flag.CREDITS.hasFlag(flags)) {
            credits = null;
        }

        if (Flag.ANIMATEDTILES.hasFlag(flags)) {
            tileEntries = null;
        }

        if (Flag.LOGO.hasFlag(flags)) {
            logos = null;
        }

        this.loadedFlags = this.loadedFlags ^ flags;
    }

    public boolean isLoaded(int flags) {
        return (loadedFlags & flags) != 0;
    }

    public Header getHeader() {
        return header;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Map<Integer, Enemy> getEnemies() {
        return enemies;
    }

    public List<GraphicSet> getGraphicSets() {
        return graphicSets;
    }

    public PaletteManager getPaletteManager() {
        return paletteManager;
    }

    public Layer3[] getLayer3List() {
        return layer3List;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public DMATables getDmaTables() {
        return dmaTables;
    }

    public List<SamusAnimation> getSamusAnimations() {
        return samusAnimations;
    }

    public List<Pose> getPoses() {
        return poses;
    }

    public Physics getPhysics() {
        return physics;
    }

    public Credits getCredits() {
        return credits;
    }

    public List<TileEntry> getTileEntries() {
        return tileEntries;
    }

    public List<Logo> getLogos() {
        return logos;
    }

    public ByteStream getStream() {
        return stream;
    }

    public SpriteMapManager getSpriteMapManager() {
        return spriteMapManager;
    }

    public enum Flag {
        ROOMS(0b1),
        ENEMIES(0b10),
        SAMUS(0b100),
        POSES(0b1000),
        PHYSICS(0b10000),
        GRAPHICSETS(0b100000),
        PALETTES(0b1000000),
        LAYER3(0b10000000),
        PROJECTILES(0b100000000),
        CREDITS(0b1000000000),
        ANIMATEDTILES(0b10000000000),
        LOGO(0b100000000000),
        ALL(Integer.MAX_VALUE);


        public final int value;

        Flag(int value) {
            this.value = value;
        }

        public static boolean hasFlag(Flag flag, int value) {
            return (value & flag.value) == flag.value;
        }

        public boolean hasFlag(int value) {
            return (value & this.value) == this.value;
        }
    }
}
