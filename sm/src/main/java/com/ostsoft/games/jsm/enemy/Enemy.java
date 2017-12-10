package com.ostsoft.games.jsm.enemy;

import com.ostsoft.games.jsm.animation.enemy.EnemyAnimation;
import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class Enemy implements Storable, Comparable<Enemy> {
    // http://jathys.zophar.net/supermetroid/kejardon/EnemyData.txt
    public int offset;

    public int tilesLength;
    public int palettePointer;
    public int hp;
    public int damage;
    public int width; // *2 for pixels
    public int height; // *2 for pixels
    public int bankPointer; // Bank for use with other pointers
    public int hurtFrames; // from 0 to 4 DMAFrames
    public int hurtSound;
    public int hurtSound2; // ?
    public int bossNumber;
    public int initializationAi;
    public int numberOfParts;
    public int unknown1;
    public int mainMovementAiPointer;
    public int grappleReaction;
    public int hurtAiPointer;
    public int frozenAiPointer;
    public int xrayOrReserveTankAiPointer;
    public int dieAnimation;
    public int unknown2;
    public int unknown3;
    public int powerBombReactionPointer;
    public int unknown4;
    public int unknown5;
    public int unknown6;
    public int touchActionPointer;
    public int projectileActionPointer;
    public int unknown7;
    public int tilePointer;
    public int layerControl;
    public int dropChancesPointer;
    public int resistancesPointer;
    public int namePointer;

    public EnemyName enemyName;
    private EnemyAnimation enemyAnimation;

    public Enemy(int offset) {
        this.offset = offset;
    }

    @Override
    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));

        tilesLength = stream.readReversedUnsignedShort();
        palettePointer = stream.readReversedUnsignedShort(); // Bank is added in the end of the constructor because the bank i read after this one
        hp = stream.readReversedUnsignedShort();
        damage = stream.readReversedUnsignedShort();
        width = stream.readReversedUnsignedShort();
        height = stream.readReversedUnsignedShort();
        bankPointer = stream.readUnsignedByte();
        hurtFrames = stream.readUnsignedByte();
        hurtSound = stream.readUnsignedByte();
        hurtSound2 = stream.readUnsignedByte();
        bossNumber = stream.readReversedUnsignedShort();
        initializationAi = stream.readReversedUnsignedShort();
        numberOfParts = stream.readReversedUnsignedShort();
        unknown1 = stream.readReversedUnsignedShort();
        mainMovementAiPointer = stream.readReversedUnsignedShort();
        grappleReaction = stream.readReversedUnsignedShort();
        hurtAiPointer = stream.readReversedUnsignedShort();
        frozenAiPointer = stream.readReversedUnsignedShort();
        xrayOrReserveTankAiPointer = stream.readReversedUnsignedShort();
        dieAnimation = stream.readReversedUnsignedShort();
        unknown2 = stream.readReversedUnsignedShort();
        unknown3 = stream.readReversedUnsignedShort();
        powerBombReactionPointer = stream.readReversedUnsignedShort();
        unknown4 = stream.readReversedUnsignedShort();
        unknown5 = stream.readReversedUnsignedShort();
        unknown6 = stream.readReversedUnsignedShort();
        touchActionPointer = stream.readReversedUnsignedShort();
        projectileActionPointer = stream.readReversedUnsignedShort();
        unknown7 = stream.readReversedUnsignedShort();
        tilePointer = stream.readReversedUnsigned3Bytes();
        layerControl = stream.readUnsignedByte();
        dropChancesPointer = stream.readReversedUnsignedShort();
        resistancesPointer = stream.readReversedUnsignedShort();
        namePointer = stream.readReversedUnsignedShort() | (0xB4 << 16);

        palettePointer = palettePointer | (bankPointer << 16);

        enemyAnimation = new EnemyAnimation(this);
        enemyAnimation.load(stream);
    }

    @Override
    public void save(ByteStream stream) {

    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getSize() {
        return 0x40;
    }

    public void printDebug() {
        System.out.println("Enemy pointer: 0x" + Integer.toHexString(offset & 0xFFFF).toUpperCase() + "  " + (offset & 0xFFFF));
        System.out.println("Enemy name offset: 0x" + Integer.toHexString(BitHelper.snesToPc(namePointer)).toUpperCase() + "  " + BitHelper.snesToPc(namePointer));

        System.out.println("Bank: 0x" + Integer.toHexString(bankPointer).toUpperCase());
        if (enemyName != null) {
            System.out.println("Name: " + enemyName.name);
        }
        System.out.println("Health: " + hp);
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);

    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(getOffset() & 0xFFFF).toUpperCase();
    }

    public EnemyAnimation getEnemyAnimation() {
        return enemyAnimation;
    }

    @Override
    public int compareTo(Enemy enemy) {
        return this.toString().compareTo(enemy.toString());
    }
}
