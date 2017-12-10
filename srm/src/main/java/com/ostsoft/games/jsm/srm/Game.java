package com.ostsoft.games.jsm.srm;

import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.ByteStream;

public class Game implements Storable {
    private int offset;

    private int shootButton, jumpButton, dashButton, itemCancelButton, itemSelectButton, angleDownButton, angleUpButton;

    private int energy, maxEnergy;
    private int reserveEnergy, maxReserveEnergy;

    private int missiles, maxMissiles;
    private int superMissiles, maxSuperMissiles;
    private int powerBombs, maxPowerBombs;

    private StartFlag startFlag = StartFlag.CERES;

    private int savePoint, saveArea;

    private int hours, minutes;

    private int language;
    private int moonWalk;
    private int iconCancel;

    public Game(int offset) {
        this.offset = offset;
    }

    @Override
    public void load(ByteStream stream) {
        stream.setPosition(offset + 0x10);
        shootButton = stream.readReversedUnsignedShort();
        jumpButton = stream.readReversedUnsignedShort();
        dashButton = stream.readReversedUnsignedShort();
        itemCancelButton = stream.readReversedUnsignedShort();
        itemSelectButton = stream.readReversedUnsignedShort();
        angleDownButton = stream.readReversedUnsignedShort();
        angleUpButton = stream.readReversedUnsignedShort();

        stream.setPosition(offset + 0x20);
        energy = stream.readReversedUnsignedShort();
        maxEnergy = stream.readReversedUnsignedShort();

        missiles = stream.readReversedUnsignedShort();
        maxMissiles = stream.readReversedUnsignedShort();

        superMissiles = stream.readReversedUnsignedShort();
        maxSuperMissiles = stream.readReversedUnsignedShort();

        powerBombs = stream.readReversedUnsignedShort();
        maxPowerBombs = stream.readReversedUnsignedShort();

        stream.setPosition(offset + 0x32);
        maxReserveEnergy = stream.readReversedUnsignedShort();
        reserveEnergy = stream.readReversedUnsignedShort();

        stream.setPosition(offset + 0x3C);
        minutes = stream.readReversedUnsignedShort();
        hours = stream.readReversedUnsignedShort();

        stream.setPosition(offset + 0x40);
        language = stream.readUnsignedByte();

        stream.setPosition(offset + 0x42);
        moonWalk = stream.readUnsignedByte();

        stream.setPosition(offset + 0x48);
        iconCancel = stream.readUnsignedByte();

        stream.setPosition(offset + 0x154);
        startFlag = StartFlag.getStartFlag(stream.readUnsignedByte());

        stream.setPosition(offset + 0x156);
        savePoint = stream.readReversedUnsignedShort();
        saveArea = stream.readReversedUnsignedShort();

    }

    @Override
    public void save(ByteStream stream) {
        stream.setPosition(offset + 0x10);
        stream.writeUnsignedReversedShort(shootButton);
        stream.writeUnsignedReversedShort(jumpButton);
        stream.writeUnsignedReversedShort(dashButton);
        stream.writeUnsignedReversedShort(itemCancelButton);
        stream.writeUnsignedReversedShort(itemSelectButton);
        stream.writeUnsignedReversedShort(angleDownButton);
        stream.writeUnsignedReversedShort(angleUpButton);

        stream.setPosition(offset + 0x20);
        stream.writeUnsignedReversedShort(energy);
        stream.writeUnsignedReversedShort(maxEnergy);

        stream.writeUnsignedReversedShort(missiles);
        stream.writeUnsignedReversedShort(maxMissiles);

        stream.writeUnsignedReversedShort(superMissiles);
        stream.writeUnsignedReversedShort(maxSuperMissiles);

        stream.writeUnsignedReversedShort(powerBombs);
        stream.writeUnsignedReversedShort(maxPowerBombs);

        stream.setPosition(offset + 0x32);
        stream.writeUnsignedReversedShort(maxReserveEnergy);
        stream.writeUnsignedReversedShort(reserveEnergy);

        stream.setPosition(offset + 0x3C);
        stream.writeUnsignedReversedShort(minutes);
        stream.writeUnsignedReversedShort(hours);

        stream.setPosition(offset + 0x40);
        stream.writeUnsignedByte(language);

        stream.setPosition(offset + 0x42);
        stream.writeUnsignedByte(moonWalk);

        stream.setPosition(offset + 0x48);
        stream.writeUnsignedByte(iconCancel);

        stream.setPosition(offset + 0x154);
        stream.writeUnsignedByte(startFlag.bit);

        stream.setPosition(offset + 0x156);
        stream.writeUnsignedReversedShort(savePoint);
        stream.writeUnsignedReversedShort(saveArea);


    }

    public void setSaveArea(SaveArea saveArea) {
        this.saveArea = saveArea.value;
    }

    public void setDefaultController() {
        shootButton = 0x0040;
        jumpButton = 0x0080;
        dashButton = 0x8000;
        itemCancelButton = 0x4000;
        itemSelectButton = 0x2000;
        angleDownButton = 0x0020;
        angleUpButton = 0x0010;
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
        return 0x65C;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getReserveEnergy() {
        return reserveEnergy;
    }

    public void setReserveEnergy(int reserveEnergy) {
        this.reserveEnergy = reserveEnergy;
    }

    public int getMaxReserveEnergy() {
        return maxReserveEnergy;
    }

    public void setMaxReserveEnergy(int maxReserveEnergy) {
        this.maxReserveEnergy = maxReserveEnergy;
    }

    public int getMissiles() {
        return missiles;
    }

    public void setMissiles(int missiles) {
        this.missiles = missiles;
    }

    public int getMaxMissiles() {
        return maxMissiles;
    }

    public void setMaxMissiles(int maxMissiles) {
        this.maxMissiles = maxMissiles;
    }

    public int getSuperMissiles() {
        return superMissiles;
    }

    public void setSuperMissiles(int superMissiles) {
        this.superMissiles = superMissiles;
    }

    public int getMaxSuperMissiles() {
        return maxSuperMissiles;
    }

    public void setMaxSuperMissiles(int maxSuperMissiles) {
        this.maxSuperMissiles = maxSuperMissiles;
    }

    public int getPowerBombs() {
        return powerBombs;
    }

    public void setPowerBombs(int powerBombs) {
        this.powerBombs = powerBombs;
    }

    public int getMaxPowerBombs() {
        return maxPowerBombs;
    }

    public void setMaxPowerBombs(int maxPowerBombs) {
        this.maxPowerBombs = maxPowerBombs;
    }

    public int getSavePoint() {
        return savePoint;
    }

    public void setSavePoint(int savePoint) {
        this.savePoint = savePoint;
    }

    public int getSaveArea() {
        return saveArea;
    }

    public void setSaveArea(int saveArea) {
        this.saveArea = saveArea;
    }

    public int getShootButton() {
        return shootButton;
    }

    public void setShootButton(int shootButton) {
        this.shootButton = shootButton;
    }

    public int getJumpButton() {
        return jumpButton;
    }

    public void setJumpButton(int jumpButton) {
        this.jumpButton = jumpButton;
    }

    public int getDashButton() {
        return dashButton;
    }

    public void setDashButton(int dashButton) {
        this.dashButton = dashButton;
    }

    public int getItemCancelButton() {
        return itemCancelButton;
    }

    public void setItemCancelButton(int itemCancelButton) {
        this.itemCancelButton = itemCancelButton;
    }

    public int getItemSelectButton() {
        return itemSelectButton;
    }

    public void setItemSelectButton(int itemSelectButton) {
        this.itemSelectButton = itemSelectButton;
    }

    public int getAngleDownButton() {
        return angleDownButton;
    }

    public void setAngleDownButton(int angleDownButton) {
        this.angleDownButton = angleDownButton;
    }

    public int getAngleUpButton() {
        return angleUpButton;
    }

    public void setAngleUpButton(int angleUpButton) {
        this.angleUpButton = angleUpButton;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getMoonWalk() {
        return moonWalk;
    }

    public void setMoonWalk(int moonWalk) {
        this.moonWalk = moonWalk;
    }

    public int getIconCancel() {
        return iconCancel;
    }

    public void setIconCancel(int iconCancel) {
        this.iconCancel = iconCancel;
    }

    public StartFlag getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(StartFlag startFlag) {
        this.startFlag = startFlag;
    }

    public enum StartFlag {
        INTRO(0x00),
        CERES(0x1F), // Skips intro
        ZEBES(0x05),
        ZEBES_NO_HEXAGON(0x06),;

        public final int bit;

        StartFlag(int bit) {
            this.bit = bit;
        }

        public static StartFlag getStartFlag(int i) {
            for (StartFlag startFlag : StartFlag.values()) {
                if (startFlag.bit == i) {
                    return startFlag;
                }
            }
            return null;
        }
    }

    public enum SaveArea {
        CRATERIA(0x00),
        BRINSTAR(0x01),
        NORFAIR(0x02),
        WRECKED_SHIP(0x03),
        MARIDIA(0x04),
        TOURIAN(0x05);

        public final int value;

        SaveArea(int value) {
            this.value = value;
        }

        public static SaveArea getSaveArea(int i) {
            for (SaveArea saveArea : SaveArea.values()) {
                if (saveArea.value == i) {
                    return saveArea;
                }
            }
            return null;
        }
    }
}
