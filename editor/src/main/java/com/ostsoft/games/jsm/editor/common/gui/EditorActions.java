package com.ostsoft.games.jsm.editor.common.gui;

import com.ostsoft.games.jsm.RoomList;
import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.editor.Editor;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.Progress;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;
import com.ostsoft.games.jsm.editor.common.observer.ObserverPanel;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;
import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.common.util.ResourceUtil;
import com.ostsoft.games.jsm.editor.credits.CreditsPanel;
import com.ostsoft.games.jsm.editor.level.Layer3Panel;
import com.ostsoft.games.jsm.editor.level.LevelPanel;
import com.ostsoft.games.jsm.editor.logo.LogoPanel;
import com.ostsoft.games.jsm.editor.options.OptionsFrame;
import com.ostsoft.games.jsm.editor.palette.PalettePanel;
import com.ostsoft.games.jsm.editor.samus.PhysicsPanel;
import com.ostsoft.games.jsm.editor.samus.PosePanel;
import com.ostsoft.games.jsm.editor.samus.ProjectileViewerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.FutureTask;

public class EditorActions implements Observer {
    private final Editor editor;
    private final TransferActionListener transferActionListener = new TransferActionListener();

    private final Action loadAction = new LoadAction();
    private final Action saveAction = new SaveAction();
    private final Action saveAsAction = new SaveAsAction();
    private final Action closeAction = new CloseAction();
    private final Action exitAction = new ExitAction();

    private final Action undoAction = new UndoAction();
    private final Action redoAction = new RedoAction();
    private final Action cutAction = new CutAction();
    private final Action copyAction = new CopyAction();
    private final Action pasteAction = new PasteAction();
    private final Action deleteAction = new DeleteAction();

    private final Action insertAction = new InsertAction();
    private final Action removeAction = new RemoveAction();
    private final Action propertiesAction = new PropertiesAction();

    private final Action zoomInAction = new ZoomInAction();
    private final Action zoomOutAction = new ZoomOutAction();

    private final Action optionsAction = new OptionsAction();

    private final Action newWindowAction = new NewWindowAction();
    private final Action closeWindowAction = new CloseWindowAction();
    private final Action newPoseEditorAction = new NewPoseEdtiorAction();
    private final Action newCreditsEditorAction = new NewCreditsEditorAction();
    private final Action newPaletteEditorAction = new NewPaletteEditorAction();
    private final Action newPhysicsEditorAction = new NewPhysicsEditorAction();
    private final Action newLogoEditorAction = new NewLogoEditorAction();

    private final Action newLevelViewerAction = new NewLevelViewerAction();
    private final Action newLayer3ViewerAction = new NewLayer3ViewerAction();
    private final Action newProjectileViewerAction = new NewProjectileViewerAction();
    private final Action newObserverAction = new NewObserverAction();
    private final Action closeTabAction = new CloseTabAction();

    private final Action helpAction = new HelpAction();

    public EditorActions(Editor editor) {
        this.editor = editor;
    }

    public void updateActions() {
        EditorData editorData = editor.getEditorData();

        boolean loaded = editorData != null;

        saveAction.setEnabled(loaded);
        saveAsAction.setEnabled(loaded);
        closeAction.setEnabled(loaded);

        if (loaded) {
            undoAction.setEnabled(editorData.getCommandCenter().canUndo());
            redoAction.setEnabled(editorData.getCommandCenter().canRedo());
        }
        else {
            undoAction.setEnabled(false);
            redoAction.setEnabled(false);
        }

        cutAction.setEnabled(loaded);
        copyAction.setEnabled(loaded);
        pasteAction.setEnabled(loaded);
        deleteAction.setEnabled(loaded);

        zoomInAction.setEnabled(loaded);
        zoomOutAction.setEnabled(loaded);

        optionsAction.setEnabled(loaded);

        newWindowAction.setEnabled(loaded);
        newPoseEditorAction.setEnabled(loaded);
        newCreditsEditorAction.setEnabled(loaded);
        newPaletteEditorAction.setEnabled(loaded);
        newPhysicsEditorAction.setEnabled(loaded);
        newLogoEditorAction.setEnabled(loaded);

        newLevelViewerAction.setEnabled(loaded);
        newLayer3ViewerAction.setEnabled(loaded);
        newProjectileViewerAction.setEnabled(loaded);
        newObserverAction.setEnabled(loaded);
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (eventType == EventType.UNDO_REDO_CHANGED) {
            undoAction.setEnabled(editor.getEditorData().getCommandCenter().canUndo());
            redoAction.setEnabled(editor.getEditorData().getCommandCenter().canRedo());
        }
    }

    public Action getLoadAction() {
        return loadAction;
    }

    public Action getSaveAction() {
        return saveAction;
    }

    public Action getSaveAsAction() {
        return saveAsAction;
    }

    public Action getCloseAction() {
        return closeAction;
    }

    public Action getExitAction() {
        return exitAction;
    }

    public Action getUndoAction() {
        return undoAction;
    }

    public Action getRedoAction() {
        return redoAction;
    }

    public Action getCutAction() {
        return cutAction;
    }

    public Action getCopyAction() {
        return copyAction;
    }

    public Action getPasteAction() {
        return pasteAction;
    }

    public Action getDeleteAction() {
        return deleteAction;
    }

    public Action getInsertAction() {
        return insertAction;
    }

    public Action getRemoveAction() {
        return removeAction;
    }

    public Action getPropertiesAction() {
        return propertiesAction;
    }

    public Action getZoomInAction() {
        return zoomInAction;
    }

    public Action getZoomOutAction() {
        return zoomOutAction;
    }

    public Action getOptionsAction() {
        return optionsAction;
    }

    public Action getNewWindowAction() {
        return newWindowAction;
    }

    public Action getCloseWindowAction() {
        return closeWindowAction;
    }

    public Action getNewPoseEditorAction() {
        return newPoseEditorAction;
    }


    public Action getNewPhysicsEditorAction() {
        return newPhysicsEditorAction;
    }

    public Action getNewLogoEditorAction() {
        return newLogoEditorAction;
    }

    public Action getNewLevelViewerAction() {
        return newLevelViewerAction;
    }

    public Action getNewLayer3ViewerAction() {
        return newLayer3ViewerAction;
    }

    public Action getNewProjectileViewerAction() {
        return newProjectileViewerAction;
    }

    public Action getNewCreditsEditorAction() {
        return newCreditsEditorAction;
    }

    public Action getNewPaletteEditorAction() {
        return newPaletteEditorAction;
    }

    public Action getNewObserverAction() {
        return newObserverAction;
    }

    public Action getCloseTabAction() {
        return closeTabAction;
    }

    public Action getHelpAction() {
        return helpAction;
    }

    private void executePanelEvent(PanelEvent panelEvent) {
        Component selectedComponent = editor.getTabbedPane().getSelectedComponent();
        if (selectedComponent instanceof EditorPanel) {
            ((EditorPanel) selectedComponent).handlePanelEvent(panelEvent);
        }
    }

    private class LoadAction extends AbstractAction {
        public LoadAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
            putValue(NAME, "Load");
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(SHORT_DESCRIPTION, "Load rom");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/document-open.png"));
        }

        public void actionPerformed(ActionEvent e) {
            EditorData editorData = editor.getEditorData();
            if (editorData != null) {
                editorData.fireEvent(EventType.LOAD);
            }
            else {
                editor.getEditorFile().load(false);
            }
        }
    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
            putValue(NAME, "Save");
            putValue(MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(SHORT_DESCRIPTION, "Save");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/document-save.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editor.getEditorData().fireEvent(EventType.SAVE);
        }

    }

    private class SaveAsAction extends AbstractAction {

        public SaveAsAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
            putValue(NAME, "Save As");
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(SHORT_DESCRIPTION, "Save with new name");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/document-save-as.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editor.getEditorData().fireEvent(EventType.SAVE_AS);
        }

    }

    private class CloseAction extends AbstractAction {

        public CloseAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
            putValue(NAME, "Close file");
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(SHORT_DESCRIPTION, "Close file");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getEditorData().fireEvent(EventType.CLOSE);
        }

    }

    private class ExitAction extends AbstractAction {

        public ExitAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
            putValue(NAME, "Exit");
            putValue(MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(SHORT_DESCRIPTION, "Exit the program");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/system-log-out.png"));
        }

        public void actionPerformed(ActionEvent e) {
            if (editor.getEditorData() != null) {
                editor.getEditorData().fireEvent(EventType.EXIT);
            }
            else {
                editor.exit();
            }
        }

    }

    private class UndoAction extends AbstractAction {

        public UndoAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
            putValue(NAME, "Undo");
            putValue(MNEMONIC_KEY, KeyEvent.VK_U);
            putValue(SHORT_DESCRIPTION, "Undo the last action");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/edit-undo.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editor.getEditorData().getCommandCenter().undo();
        }

    }

    private class RedoAction extends AbstractAction {

        public RedoAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
            putValue(NAME, "Redo");
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(SHORT_DESCRIPTION, "Reapply the previous undo");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/edit-redo.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editor.getEditorData().getCommandCenter().redo();
        }

    }

    private class CutAction extends AbstractAction {


        public CutAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
            putValue(NAME, "Cut");
            putValue(MNEMONIC_KEY, KeyEvent.VK_T);
            putValue(SHORT_DESCRIPTION, "Remove and place on clipboard");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/edit-cut.png"));
        }

        public void actionPerformed(ActionEvent e) {
            transferActionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, (String) TransferHandler.getCutAction().getValue(Action.NAME), e.getWhen(), e.getModifiers()));
        }

    }

    private class CopyAction extends AbstractAction {

        public CopyAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
            putValue(NAME, "Copy");
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(SHORT_DESCRIPTION, "Copy to clipboard");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/edit-copy.png"));

        }

        public void actionPerformed(ActionEvent e) {
            transferActionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, (String) TransferHandler.getCopyAction().getValue(Action.NAME), e.getWhen(), e.getModifiers()));
        }

    }

    private class PasteAction extends AbstractAction {

        public PasteAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
            putValue(NAME, "Paste");
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(SHORT_DESCRIPTION, "Insert from clipboard");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/edit-paste.png"));
        }

        public void actionPerformed(ActionEvent e) {
            transferActionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, (String) TransferHandler.getPasteAction().getValue(Action.NAME), e.getWhen(), e.getModifiers()));
        }

    }

    private class DeleteAction extends AbstractAction {

        public DeleteAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
            putValue(NAME, "Delete");
            putValue(MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(SHORT_DESCRIPTION, "Delete");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/edit-delete.png"));
        }

        public void actionPerformed(ActionEvent e) {
            executePanelEvent(PanelEvent.DELETE);
        }

    }

    private class InsertAction extends AbstractAction {

        public InsertAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
            putValue(NAME, "Insert");
            putValue(MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(SHORT_DESCRIPTION, "Insert");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/list-add.png"));
        }

        public void actionPerformed(ActionEvent e) {
            executePanelEvent(PanelEvent.INSERT);
        }

    }

    private class RemoveAction extends AbstractAction {

        public RemoveAction() {
//            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
            putValue(NAME, "Remove");
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(SHORT_DESCRIPTION, "Remove");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/list-remove.png"));
        }

        public void actionPerformed(ActionEvent e) {
            executePanelEvent(PanelEvent.DELETE);
        }

    }

    private class PropertiesAction extends AbstractAction {

        public PropertiesAction() {
//            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_, 0));
            putValue(NAME, "Properies");
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(SHORT_DESCRIPTION, "Properties");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/document-properties.png"));
        }

        public void actionPerformed(ActionEvent e) {
            executePanelEvent(PanelEvent.PROPERTIES);
        }

    }

    private class HelpAction extends AbstractAction {

        public HelpAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
            putValue(NAME, "Help");
            putValue(MNEMONIC_KEY, KeyEvent.VK_H);
            putValue(SHORT_DESCRIPTION, "Show documentation");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/help-browser.png"));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().browse(URI.create("http://jsm.ostehovel.com/#help"));
            } catch (IOException e1) {
                ErrorUtil.displayStackTrace(e1);
            }
        }
    }

    private class ZoomInAction extends AbstractAction {

        public ZoomInAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(NAME, "Zoom in");
        }

        public void actionPerformed(ActionEvent e) {

            executePanelEvent(PanelEvent.ZOOM_IN);
        }

    }

    private class ZoomOutAction extends AbstractAction {

        public ZoomOutAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(NAME, "Zoom out");
        }

        public void actionPerformed(ActionEvent e) {
            executePanelEvent(PanelEvent.ZOOM_OUT);
        }

    }

    private class OptionsAction extends AbstractAction {

        public OptionsAction() {
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(NAME, "Options");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/preferences-desktop.png"));
        }

        public void actionPerformed(ActionEvent e) {
            new OptionsFrame(editor.getEditorData());
        }

    }

    private class NewWindowAction extends AbstractAction {

        public NewWindowAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_W);
            putValue(NAME, "New linked window");
            putValue(SMALL_ICON, ResourceUtil.getIcon("images/actions/window-new.png"));
        }

        public void actionPerformed(ActionEvent e) {
            new Editor(editor.getEditorData());
        }

    }

    private class CloseWindowAction extends AbstractAction {

        public CloseWindowAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            putValue(NAME, "Close window");
        }

        public void actionPerformed(ActionEvent e) {
            editor.exit();
        }

    }

    private class NewPoseEdtiorAction extends AbstractAction {

        public NewPoseEdtiorAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(NAME, "New pose editor");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getTabbedPane().addTab("Pose", new PosePanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }

    private class NewCreditsEditorAction extends AbstractAction {

        public NewCreditsEditorAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(NAME, "New credits editor");
        }

        public void actionPerformed(ActionEvent e) {
//            new Thread(new Progress(Collections.singleton(new FutureTask<>(() -> {
            editor.getTabbedPane().addTab("Credits", new CreditsPanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
//            }, null)))).start();
        }

    }

    private class NewPaletteEditorAction extends AbstractAction {

        public NewPaletteEditorAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
            putValue(NAME, "New palette editor");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getTabbedPane().addTab("Palette", new PalettePanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }

    private class NewPhysicsEditorAction extends AbstractAction {

        public NewPhysicsEditorAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_Y);
            putValue(NAME, "New physics editor");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getTabbedPane().addTab("Physics", new PhysicsPanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }

    private class NewLogoEditorAction extends AbstractAction {

        public NewLogoEditorAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(NAME, "New logo editor");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getTabbedPane().addTab("Logo", new LogoPanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }

    private class NewLevelViewerAction extends AbstractAction {

        public NewLevelViewerAction() {
//            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(NAME, "New level viewer");
        }

        public void actionPerformed(ActionEvent e) {
            ArrayList<FutureTask> tasks = new ArrayList<>();
            tasks.add(new FutureTask<>(() -> {
                editor.getEditorData().getSuperMetroid().load(SuperMetroid.Flag.GRAPHICSETS.value, Collections.emptyList(), Collections.emptyList());
            }, null));

            for (Integer roomOffset : RoomList.getRoomList()) {
                tasks.add(new FutureTask<>(() -> {
                    editor.getEditorData().getSuperMetroid().load(SuperMetroid.Flag.ROOMS.value, Collections.singletonList(roomOffset), Collections.emptyList());
                }, null));
            }

            tasks.add(new FutureTask<>(() -> {
                editor.getTabbedPane().addTab("Level", new LevelPanel(editor.getEditorData()));
                editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);

            }, null));

            new Thread(new Progress(tasks)).start();
        }

    }

    private class NewLayer3ViewerAction extends AbstractAction {

        public NewLayer3ViewerAction() {
//            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_3);
            putValue(NAME, "New layer 3 viewer");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getTabbedPane().addTab("Layer 3", new Layer3Panel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }

    private class NewProjectileViewerAction extends AbstractAction {

        public NewProjectileViewerAction() {
//            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_P);
            putValue(NAME, "New projectile viewer");
        }

        public void actionPerformed(ActionEvent e) {
            editor.getTabbedPane().addTab("Projectile", new ProjectileViewerPanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }

    private class CloseTabAction extends AbstractAction {

        public CloseTabAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
            putValue(NAME, "Close Tab");
        }

        public void actionPerformed(ActionEvent e) {
            if (editor.getTabbedPane().getTabCount() > 1) {
                editor.getTabbedPane().removeTabAt(editor.getTabbedPane().getSelectedIndex());
            }
        }

    }

    private class NewObserverAction extends AbstractAction {

        public NewObserverAction() {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(NAME, "New observer (debug)");
        }

        public void actionPerformed(ActionEvent e) {

            editor.getTabbedPane().addTab("Observer", new ObserverPanel(editor.getEditorData()));
            editor.getTabbedPane().setSelectedIndex(editor.getTabbedPane().getTabCount() - 1);
        }

    }
}
