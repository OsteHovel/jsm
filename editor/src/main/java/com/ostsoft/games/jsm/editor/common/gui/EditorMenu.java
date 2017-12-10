package com.ostsoft.games.jsm.editor.common.gui;

import com.ostsoft.games.jsm.Pointer;
import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.common.AttTile;
import com.ostsoft.games.jsm.common.HTileMap;
import com.ostsoft.games.jsm.common.SimpleTile;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.common.TileMap;
import com.ostsoft.games.jsm.credits.CreditsTile;
import com.ostsoft.games.jsm.editor.Editor;
import com.ostsoft.games.jsm.editor.TestRom;
import com.ostsoft.games.jsm.editor.animatedtiles.AnimatedTilesPanel;
import com.ostsoft.games.jsm.editor.common.About;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.enemy.EnemyPanel;
import com.ostsoft.games.jsm.editor.palette.PointerPanel;
import com.ostsoft.games.jsm.editor.samus.ProjectilePanel;
import com.ostsoft.games.jsm.editor.tilemap.TileMapPanel;
import com.ostsoft.games.jsm.editor.tilemap.TileMapPanelData;
import com.ostsoft.games.jsm.palette.PaletteEnum;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.CompressUtil;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class EditorMenu extends JMenuBar {

    public EditorMenu(Editor editor) {
        super();
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "zoomInAction");
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "zoomOutAction");
        getActionMap().put("zoomInAction", editor.getEditorActions().getZoomInAction());
        getActionMap().put("zoomOutAction", editor.getEditorActions().getZoomOutAction());

        JMenu mnFile = new JMenu("File");
        mnFile.setMnemonic(KeyEvent.VK_F);
        add(mnFile);
        {
            mnFile.add(new JMenuItem(editor.getEditorActions().getLoadAction()));
            mnFile.add(new JMenuItem(editor.getEditorActions().getSaveAction()));
            mnFile.add(new JMenuItem(editor.getEditorActions().getSaveAsAction()));

            mnFile.add(new JSeparator());

            mnFile.add(new JMenuItem(editor.getEditorActions().getCloseAction()));

            mnFile.add(new JSeparator());

            mnFile.add(new JMenuItem(editor.getEditorActions().getExitAction()));
        }

        JMenu mnEdit = new JMenu("Edit");
        mnEdit.setMnemonic(KeyEvent.VK_E);
        add(mnEdit);
        {
            mnEdit.add(new JMenuItem(editor.getEditorActions().getUndoAction()));
            mnEdit.add(new JMenuItem(editor.getEditorActions().getRedoAction()));
            mnEdit.add(new JSeparator());

            mnEdit.add(new JMenuItem(editor.getEditorActions().getCutAction()));
            mnEdit.add(new JMenuItem(editor.getEditorActions().getCopyAction()));
            mnEdit.add(new JMenuItem(editor.getEditorActions().getPasteAction()));

            mnEdit.add(new JSeparator());
            mnEdit.add(new JMenuItem(editor.getEditorActions().getDeleteAction()));
        }

        JMenu mnView = new JMenu("View");
        mnView.setMnemonic(KeyEvent.VK_V);
        add(mnView);
        {
            mnView.add(new JMenuItem(editor.getEditorActions().getZoomInAction()));
            mnView.add(new JMenuItem(editor.getEditorActions().getZoomOutAction()));
        }

        JMenu mnTools = new JMenu("Tools");
        List<Component> mnToolsDefault = new ArrayList<>();
        mnTools.setMnemonic(KeyEvent.VK_T);
        mnTools.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                mnTools.removeAll();
                Component selectedComponent = editor.getTabbedPane().getSelectedComponent();
                if (selectedComponent != null && selectedComponent instanceof EditorPanel) {
                    List<Component> toolsMenu = ((EditorPanel) selectedComponent).getToolsMenu();
                    toolsMenu.forEach(mnTools::add);
                    if (toolsMenu.size() > 0) {
                        mnTools.add(new JSeparator());
                    }
                }

                mnToolsDefault.forEach(mnTools::add);
                mnTools.revalidate();
                mnTools.repaint();
                mnTools.doClick();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        add(mnTools);
        {
            mnToolsDefault.add(new JMenuItem(editor.getEditorActions().getOptionsAction()));
        }

        JMenu mnWindow = new JMenu("Window");
        mnWindow.setMnemonic(KeyEvent.VK_W);
        add(mnWindow);
        {
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewWindowAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getCloseWindowAction()));

            mnWindow.add(new JSeparator());

            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewPoseEditorAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewCreditsEditorAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewPaletteEditorAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewPhysicsEditorAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewLogoEditorAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getCloseTabAction()));

            mnWindow.add(new JSeparator());

            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewLevelViewerAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewLayer3ViewerAction()));
            mnWindow.add(new JMenuItem(editor.getEditorActions().getNewProjectileViewerAction()));

            if (About.DEBUG) {
                mnWindow.add(new JSeparator());

                JMenuItem mntmObserver = new JMenuItem();
                mntmObserver.setAction(editor.getEditorActions().getNewObserverAction());
                mnWindow.add(mntmObserver);

                JMenuItem mntmAnimatedTile = new JMenuItem("New animated tile tab");
//                mntmAnimatedTile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
                mntmAnimatedTile.addActionListener(e1 -> {
                    editor.getTabbedPane().addTab("Animated Tile", new AnimatedTilesPanel(editor.getEditorData()));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmAnimatedTile);

                JMenuItem mntmTilemap = new JMenuItem("New tilemap editor (credits)");
//                mntmTilemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
                mntmTilemap.addActionListener(e1 -> {

                    TileMapPanelData tileMapPanelData = new TileMapPanelData(editor.getEditorData());
                    tileMapPanelData.setTileType(CreditsTile.class);
                    tileMapPanelData.setTiles(editor.getEditorData().getSuperMetroid().getCredits().getTiles());
                    tileMapPanelData.setTileMaps(editor.getEditorData().getSuperMetroid().getCredits().getTileMaps());

                    editor.getTabbedPane().addTab("Tilemap", new TileMapPanel(editor.getEditorData(), tileMapPanelData));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmTilemap);

                JMenuItem mntmTestTilemap = new JMenuItem("New tilemap editor (Pause)");
//                mntmTestTilemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
                mntmTestTilemap.addActionListener(e1 -> {
                    ByteStream stream = editor.getEditorData().getSuperMetroid().getStream();
                    byte[] tilesBytes = new byte[0x4FFF];
                    stream.setPosition(BitHelper.snesToPc(Pointer.PauseTiles.pointer));
                    stream.read(tilesBytes);

                    int bpp = 4;
                    List<Tile> tiles = new ArrayList<>();
                    for (int i = 0; i < tilesBytes.length / (bpp * 8); i++) {
                        byte[] tileBytes = new byte[(bpp * 8)];
                        System.arraycopy(tilesBytes, i * tileBytes.length, tileBytes, 0, tileBytes.length);
                        tiles.add(new SimpleTile(tileBytes, bpp));
                    }

                    List<TileMap> tileMaps = new ArrayList<>();
                    HTileMap tileMap = new HTileMap(Pointer.SamusPauseTileMap.pointer, 0x800, 32, tiles);
                    tileMap.load(stream);
                    tileMaps.add(tileMap);

                    HTileMap tileMap2 = new HTileMap(Pointer.MapPauseTileMap.pointer, 0x800, 32, tiles);
                    tileMap2.load(stream);
                    tileMaps.add(tileMap2);

                    TileMapPanelData tileMapPanelData = new TileMapPanelData(editor.getEditorData());
                    tileMapPanelData.setTileType(AttTile.class);
                    tileMapPanelData.setTiles(tiles);
                    tileMapPanelData.setTileMaps(tileMaps);
                    tileMapPanelData.setPalette(editor.getEditorData().getSuperMetroid().getPaletteManager().getPalette(PaletteEnum.PAUSE_SCREEN));

                    editor.getTabbedPane().addTab("Tilemap", new TileMapPanel(editor.getEditorData(), tileMapPanelData));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmTestTilemap);

                JMenuItem mntmTest2Tilemap = new JMenuItem("New tilemap editor (Intro)");
//                mntmTest2Tilemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
                mntmTest2Tilemap.addActionListener(e1 -> {
                    ByteStream stream = editor.getEditorData().getSuperMetroid().getStream();

                    byte[] tilesBytes = CompressUtil.decompress(editor.getEditorData().getSuperMetroid().getStream(), BitHelper.snesToPc(Pointer.Intro2Tiles.pointer));

                    int bpp = 4;
                    List<Tile> tiles = new ArrayList<>();
                    for (int i = 0; i < tilesBytes.length / (bpp * 8); i++) {
                        byte[] tileBytes = new byte[(bpp * 8)];
                        System.arraycopy(tilesBytes, i * tileBytes.length, tileBytes, 0, tileBytes.length);
                        tiles.add(new SimpleTile(tileBytes, bpp));
                    }
                    List<TileMap> tileMaps = new ArrayList<>();

                    HTileMap tileMap = new HTileMap(Pointer.Intro2Tilemap.pointer, 32, tiles);
                    tileMap.load(stream);
                    tileMaps.add(tileMap);

                    TileMapPanelData tileMapPanelData = new TileMapPanelData(editor.getEditorData());
                    tileMapPanelData.setTileType(AttTile.class);
                    tileMapPanelData.setTiles(tiles);
                    tileMapPanelData.setTileMaps(tileMaps);
                    tileMapPanelData.setPalette(editor.getEditorData().getSuperMetroid().getPaletteManager().getPalette(PaletteEnum.INTRO2));

                    editor.getTabbedPane().addTab("Tilemap", new TileMapPanel(editor.getEditorData(), tileMapPanelData));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmTest2Tilemap);

                JMenuItem mntmTest3Tilemap = new JMenuItem("New tilemap editor (Galaxy Is At Peace)");
//                mntmTest2Tilemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
                mntmTest3Tilemap.addActionListener(e1 -> {
                    ByteStream stream = editor.getEditorData().getSuperMetroid().getStream();
                    byte[] tilesBytes = CompressUtil.decompress(editor.getEditorData().getSuperMetroid().getStream(), BitHelper.snesToPc(Pointer.TitleScreenTiles.pointer));
                    int bpp = 4;
                    List<Tile> tiles = new ArrayList<>();
                    for (int i = 0; i < tilesBytes.length / (bpp * 8); i++) {
                        byte[] tileBytes = new byte[(bpp * 8)];
                        System.arraycopy(tilesBytes, i * tileBytes.length, tileBytes, 0, tileBytes.length);
                        tiles.add(new SimpleTile(tileBytes, bpp));
                    }
//                    List<Tile> tiles = editor.getEditorData().getSuperMetroid().getCredits().getTiles();

                    List<TileMap> tileMaps = new ArrayList<>();
                    HTileMap tileMap = new HTileMap(Pointer.GalaxyIsAtPeace.pointer, 32, tiles);
                    tileMap.load(stream);
                    tileMaps.add(tileMap);

                    TileMapPanelData tileMapPanelData = new TileMapPanelData(editor.getEditorData());
                    tileMapPanelData.setTileType(AttTile.class);
                    tileMapPanelData.setTiles(tiles);
                    tileMapPanelData.setTileMaps(tileMaps);
                    tileMapPanelData.setPalette(editor.getEditorData().getSuperMetroid().getPaletteManager().getPalette(PaletteEnum.INTRO2));

                    editor.getTabbedPane().addTab("Tilemap", new TileMapPanel(editor.getEditorData(), tileMapPanelData));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmTest3Tilemap);

                JMenuItem mntmPointer = new JMenuItem("New pointer tab");
                mntmPointer.addActionListener(e1 -> {
                    editor.getTabbedPane().addTab("Pointers", new PointerPanel(editor.getEditorData()));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmPointer);

                JMenuItem mntmEnemies = new JMenuItem("New enemies tab");
                mntmEnemies.addActionListener(e1 -> {
                    editor.getTabbedPane().addTab("Enemy", new EnemyPanel(editor.getEditorData()));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmEnemies);

                JMenuItem mntmProjectilePanel = new JMenuItem("New projectile tab");
                mntmProjectilePanel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
                mntmProjectilePanel.addActionListener(e1 -> {
                    editor.getTabbedPane().addTab("Projectile", new ProjectilePanel(editor.getEditorData()));
                    editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
                });
                mnWindow.add(mntmProjectilePanel);


                JMenuItem mntmTest = new JMenuItem("Test");
                mntmTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
                mntmTest.addActionListener(e -> {
                    TestRom testRom = new TestRom(editor.getEditorData().getSuperMetroid());
                    testRom.save();
                    testRom.run();

                });
                mnWindow.add(mntmTest);

                JMenuItem mntmGraphicSetTest = new JMenuItem("GraphicSet test");
                mntmGraphicSetTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
                mntmGraphicSetTest.addActionListener(e -> {

                    SuperMetroid superMetroid = editor.getEditorData().getSuperMetroid();
                    superMetroid.load(SuperMetroid.Flag.GRAPHICSETS.value);
                    superMetroid.getGraphicSets().get(0).printDebug();
                });
                mnWindow.add(mntmGraphicSetTest);
            }
        }
        JMenu mnHelp = new JMenu("Help");
        mnHelp.setMnemonic(KeyEvent.VK_H);
        add(mnHelp);
        {
            mnHelp.add(new JMenuItem(editor.getEditorActions().getHelpAction()));

            mnHelp.add(new JSeparator());

            JMenuItem mntmAbout = new JMenuItem("About");
            mntmAbout.setMnemonic(KeyEvent.VK_A);
            mntmAbout.addActionListener(e -> new About());
            mnHelp.add(mntmAbout);
        }
    }
}
