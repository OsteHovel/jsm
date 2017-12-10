package com.ostsoft.games.jsm;

import com.ostsoft.games.jsm.common.Storable;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;

public class Header implements Storable {
    private int offset = 0x80FFC0;
    private String title; // 22-bytes
    private int romType;
    private int romSize;
    private int sramSize;
    private int countryCode;
    private int publisher;
    private int version;
    private int checksum;

    /*
     ROM Type:
ROM = 00
ROM+RAM = 01
ROM+SRAM = 02
ROM+DSP1 = 03
ROM+DSP1+RAM = 04
ROM+DSP1+SRAM = 05
ROM+FX Chip = 13
ROM+RAM+Super GameBoy = [need to do more research here]
ROM+DSP2 = F6

ROM Size:
00-07 = Invalid Size
08 = 2MBit
09 = 4MBit
0A = 8MBit
0B = 10/12/14MBit
0C = 24/32MBit
0D = 48/64MBit
0F-FF = Do not know any factory ROM with this size

SRAM Size:
NONE = 00
16KBit = 01
32KBit = 02
64KBit = 03
128KBit = 04
256KBit = 05

Country code list:
Japan (NTSC) = 00
USA (NTSC) = 01
Euro+Asia+Oceania (PAL) = 02
Sweden (PAL) = 03
Finland (PAL) = 04
Denmark (PAL) = 05
France (PAL) = 06
Holland (PAL) = 07
Spain (PAL) = 08
German+Austria (PAL) = 09
Italy (PAL) = 0A
Hong Kong (PAL) = 0B
Indonesia (PAL) = 0C
Korea (PAL) = 0D

Publisher (Only listing a few examples):
Acclaim = 33
Enix = B4
TAITO = C0
Square+Squaresoft = C3
    */

    @Override
    public void load(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        title = stream.readString(22);
        romType = stream.readUnsignedByte();
        romSize = stream.readUnsignedByte();
        sramSize = stream.readUnsignedByte();
        countryCode = stream.readUnsignedByte();
        publisher = stream.readUnsignedByte();
        version = stream.readUnsignedByte();
        checksum = stream.readReversedUnsignedShort();
    }

    @Override
    public void save(ByteStream stream) {
        stream.setPosition(BitHelper.snesToPc(offset));
        stream.writeString(title);
        stream.writeUnsignedByte(romType);
        stream.writeUnsignedByte(romSize);
        stream.writeUnsignedByte(sramSize);
        stream.writeUnsignedByte(countryCode);
        stream.writeUnsignedByte(publisher);
        stream.writeUnsignedByte(version);
        stream.writeUnsignedReversedShort(checksum);
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
        return 30;
    }

    public String getTitle() {
        return title;
    }

    public int getRomType() {
        return romType;
    }

    public int getRomSize() {
        return romSize;
    }

    public int getSramSize() {
        return sramSize;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public int getPublisher() {
        return publisher;
    }

    public int getVersion() {
        return version;
    }

    public int getChecksum() {
        return checksum;
    }

    public boolean isPAL() {
        return getCountryCode() > 0x1;
    }
}
