package com.ostsoft.games.jsm.editor;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.srm.Game;
import com.ostsoft.games.jsm.srm.SRM;
import com.ostsoft.games.jsm.util.ByteStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestRom {
    private static final String EMULATORPATH = "snes9x/";
    private static final String EMULATOREXEC = EMULATORPATH + "snes9x.exe";
    private static final String SAVEPATH = EMULATORPATH + "saves/";
    private final SuperMetroid superMetroid;

    public TestRom(SuperMetroid superMetroid) {
        this.superMetroid = superMetroid;

    }

    public void save() {
        ByteStream srmStream = new ByteStream(new byte[0x2000]);
        SRM srm = new SRM();
        srm.newSRM();

        Game game = srm.getGames()[0];
        game.setDefaultController();
        game.setEnergy(1499);
        game.setMaxEnergy(1499);

        game.setReserveEnergy(400);
        game.setMaxReserveEnergy(400);

        game.setMissiles(250);
        game.setMaxMissiles(250);

        game.setSavePoint(1);
        game.setSaveArea(Game.SaveArea.BRINSTAR);
        game.setStartFlag(Game.StartFlag.ZEBES_NO_HEXAGON);

        game.setHours(50);
        game.setMinutes(40);

        srm.save(srmStream);


        try {
            FileOutputStream romOutputStream = new FileOutputStream("quick.smc");
            romOutputStream.write(superMetroid.getStream().getBuffer());
            romOutputStream.close();

            FileOutputStream srmOutputStream = new FileOutputStream(SAVEPATH + "quick.srm");
            srmOutputStream.write(srmStream.getBuffer());
            srmOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Runtime.getRuntime().exec(new String[]{EMULATOREXEC, new File("quick.smc").getAbsolutePath()});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
