package com.ostsoft.games.jsm.util;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class EnemyAnimationFrame {
    private static HashMap<Integer, List<Integer>> enemyAnimationFrame;

    private static void populateEnemyAnimationFrame() {
        enemyAnimationFrame = new HashMap<>();
        List<Integer> value;

        // Crawlers
        value = asList(0x11E260, 0x11E264, 0x11E268, 0x11E26C, 0x11E270, 0x11E27C, 0x11E280, 0x11E284, 0x11E288, 0x11E28C, 0x11E298, 0x11E29C, 0x11E2A0, 0x11E2A4, 0x11E2A8, 0x11E2B4, 0x11E2B8, 0x11E2BC, 0x11E2C0, 0x11E2C4);
        enemyAnimationFrame.put(0xDC7F, value);
        enemyAnimationFrame.put(0xDCBF, value);
        enemyAnimationFrame.put(0xDCFF, value);
        enemyAnimationFrame.put(0xDD3F, value);

        // NAMI
        enemyAnimationFrame.put(0xE73F, asList(0x1415C3, 0x1415C7, 0x1415CB, 0x1415CF, 0x1415D3, 0x1415DB, 0x1415DF, 0x1415E7, 0x1415E7, 0x1415E7, 0x1415F7, 0x1415FB, 0x1415FF, 0x141603, 0x141607, 0x14160F, 0x141613, 0x141617, 0x14161B));

        // Space pirate
        value = asList(0x197B68, 0x197B6C, 0x197B70, 0x197B74, 0x197B78, 0x197B7C, 0x197B80, 0x197B84 // Walking left
                , 0x197BCE, 0x197BD2, 0x197BD6, 0x197BDA, 0x197BDE // Looking around and turning to walk the other way, facing left.
        );
        enemyAnimationFrame.put(0xF653, value);
        enemyAnimationFrame.put(0xF693, value);
        enemyAnimationFrame.put(0xF6D3, value);
        enemyAnimationFrame.put(0xF713, value);
        enemyAnimationFrame.put(0xF753, value);
        enemyAnimationFrame.put(0xF793, value);

        // Ceres door
        enemyAnimationFrame.put(0xE23F, asList(0x1375A6, 0x1375A6, 0x1375AE, 0x1375B2 // Opening left
                , 0x137588, 0x13758C, 0x137590, 0x137594)); // Closing left

        // SPA
        enemyAnimationFrame.put(0xEA3F, asList(0x1465A9, 0x1465AD, 0x1465B1, 0x1465B5, 0x1465B9, 0x1465BD, 0x1465C1, 0x1465C5, 0x1465C9, 0x1465CD, 0x1465D1, 0x1465D5, 0x1465D9, 0x1465DD, 0x1465E5, 0x1465E9, 0x1465ED, 0x1465F1, 0x1465F5, 0x1465F9, 0x1465FD, 0x146601));

        // MERO, MELLA, MEMU
        value = asList(0x113013, 0x113017, 0x11301B, 0x11301F);
        enemyAnimationFrame.put(0xD0FF, value);
        enemyAnimationFrame.put(0xD13F, value);
        enemyAnimationFrame.put(0xD17F, value);

        // HZOOMER
        value = asList(0x11DFCB, 0x11DFCF, 0x11DFD3, 0x11DFD7, 0x11DFDB, 0x11DFDF, 0x11DFE3, 0x11DFE7, 0x11DFEB, 0x11DFEF, 0x11DFF3, 0x11DFF7, 0x11DFFB, 0x11DFFF, 0x11E003, 0x11E007, 0x11E00B, 0x11E00F, 0x11E013, 0x11E017, 0x11E01B, 0x11E01F, 0x11E023, 0x11E027, 0x11E02B, 0x11E02F);
        enemyAnimationFrame.put(0xDC3F, value);

        // GEEGA
        enemyAnimationFrame.put(0xF253, asList(0x198F10, 0x198F14, 0x198F18, 0x198F1C));

        // (FAKE)KAMER that i call SHUTTER3
        enemyAnimationFrame.put(0xD83F, asList(0x119BE9, 0x119BED, 0x119BF1, 0x119BF4 // Idle & lowering
                , 0x119BBD, 0x119BC1, 0x119BC5, 0x119BC9)); // going up


    }

    public static boolean hasEnemy(int enemy) {
        if (enemyAnimationFrame == null) {
            populateEnemyAnimationFrame();
        }
        return enemyAnimationFrame.containsKey(enemy);
    }

    public static List<Integer> getAnimationFrames(int enemy) {
        if (enemyAnimationFrame == null) {
            populateEnemyAnimationFrame();
        }
        return enemyAnimationFrame.get(enemy);
    }
}
