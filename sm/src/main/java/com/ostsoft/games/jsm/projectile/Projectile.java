package com.ostsoft.games.jsm.projectile;

import com.ostsoft.games.jsm.animation.projectile.ProjectileAnimation;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.palette.PaletteManager;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class Projectile {

    public ProjectileType projectileType;
    public DamageAndDirection damageAndDirection;
    public byte[] tiles;
    public Palette palette;

    public ProjectileAnimation[] projectileAnimations;

    public Projectile(ByteStream stream, PaletteManager paletteManager, ProjectileType projectileType) {
        this.projectileType = projectileType;
        int compensatedProjectileTypeValue = this.projectileType.value;
//        if (compensatedProjectileTypeValue > 0x0100) {
//            compensatedProjectileTypeValue /= 0x0100;
//        }
//        else if (compensatedProjectileTypeValue == 0x0100) {
//            compensatedProjectileTypeValue = 0;
//        }

        int snesAddress = projectileType.projectileMainType.value + (compensatedProjectileTypeValue * 2);
        int position1 = BitHelper.snesToPc(snesAddress);
        stream.setPosition(position1);

        int offset = stream.readReversedUnsignedShort() | (0x93 << 16);
        int position = BitHelper.snesToPc(offset);
        stream.setPosition(position);
        damageAndDirection = new DamageAndDirection(stream);

//        System.out.println("Snes: " + Integer.toHexString(snesAddress) + " Pc: " + Integer.toHexString(position1));
//        System.out.println("Snes: " + Integer.toHexString(offset) + " Pc: " + Integer.toHexString(position));
//        System.out.println(projectileType.name() + " Damage: " + damageAndDirection.damage);

        stream.setPosition(BitHelper.snesToPc(0x90C3C9 + (projectileType.value * 2)));
        int palettePointer = stream.readReversedUnsignedShort() | (0x90 << 16);

        palette = new Palette("Projectile palette " + this.projectileType.name(), palettePointer, false, 32);
        palette.load(stream);
        paletteManager.addPalette(palette);

        int tilesPointerPointer = 0x90C3B1 + (projectileType.value * 2);
        int n = BitHelper.snesToPc(tilesPointerPointer);
        stream.setPosition(n);
        int tileOffset = stream.readReversedUnsignedShort() | (0x9A << 16);

//        System.out.println("SNES: " + Integer.toHexString(tileOffset).toUpperCase());
//        System.out.println("PC: " + Integer.toHexString(BitHelper.snesToPc(tileOffset)).toUpperCase());
        tileOffset -= 48 * 32; // FIXME: Offset so BEAM weapons works, but it does not work for missiles, but they dont work anyway...
//        tileOffset -= 0x2100;
//        tileOffset += 32 * 8;

        stream.setPosition(BitHelper.snesToPc(tileOffset));
        tiles = new byte[0x4000];
        stream.read(tiles, 0, tiles.length);

        projectileAnimations = new ProjectileAnimation[10];
        for (int i = 0; i < projectileAnimations.length; i++) {
            stream.setPosition(BitHelper.snesToPc(damageAndDirection.directionPointers[i]));
            projectileAnimations[i] = new ProjectileAnimation(stream, tiles);
        }
    }

    @Override
    public String toString() {
        return projectileType.name();
    }

    public void printDebug() {
        damageAndDirection.printDebug();
    }

    public enum ProjectileType {
        POWER(0x0000, ProjectileMainType.NORMAL),
        WAVE(0x0001, ProjectileMainType.NORMAL),
        ICE(0x0002, ProjectileMainType.NORMAL),
        ICE_WAVE(0x0003, ProjectileMainType.NORMAL),
        SPACER(0x0004, ProjectileMainType.NORMAL),
        SPACER_WAVE(0x0005, ProjectileMainType.NORMAL),
        SPACER_ICE(0x0006, ProjectileMainType.NORMAL),
        SPACER_WAVE_ICE(0x0007, ProjectileMainType.NORMAL),
        PLASMA(0x0008, ProjectileMainType.NORMAL),
        PLASMA_WAVE(0x0009, ProjectileMainType.NORMAL),
        PLASMA_ICE(0x000A, ProjectileMainType.NORMAL),
        PLASMA_WAVE_ICE(0x000B, ProjectileMainType.NORMAL),

        CHARGED_POWER(0x0000, ProjectileMainType.CHARGED),
        CHARGED_WAVE(0x0001, ProjectileMainType.CHARGED),
        CHARGED_ICE(0x0002, ProjectileMainType.CHARGED),
        CHARGED_ICE_WAVE(0x0003, ProjectileMainType.CHARGED),
        CHARGED_SPACER(0x0004, ProjectileMainType.CHARGED),
        CHARGED_SPACER_WAVE(0x0005, ProjectileMainType.CHARGED),
        CHARGED_SPACER_ICE(0x0006, ProjectileMainType.CHARGED),
        CHARGED_SPACER_WAVE_ICE(0x0007, ProjectileMainType.CHARGED),
        CHARGED_PLASMA(0x0008, ProjectileMainType.CHARGED),
        CHARGED_PLASMA_WAVE(0x0009, ProjectileMainType.CHARGED),
        CHARGED_PLASMA_ICE(0x000A, ProjectileMainType.CHARGED),
        CHARGED_PLASMA_WAVE_ICE(0x000B, ProjectileMainType.CHARGED),

        MISSILE(0x0000, ProjectileMainType.CONSUMABLES),
        SUPER_MISSILE(0x0002, ProjectileMainType.CONSUMABLES);

        public final int value;
        public final ProjectileMainType projectileMainType;

        ProjectileType(int value, ProjectileMainType projectileMainType) {
            this.value = value;
            this.projectileMainType = projectileMainType;
        }
    }

    public enum ProjectileMainType {
        NORMAL(0x9383C1),
        CHARGED(0x9383D9),
        CONSUMABLES(0x9383F1);

        public final int value;

        ProjectileMainType(int value) {
            this.value = value;
        }
    }
}
