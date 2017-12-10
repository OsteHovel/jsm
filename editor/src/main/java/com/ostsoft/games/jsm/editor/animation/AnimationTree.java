package com.ostsoft.games.jsm.editor.animation;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.tree.AnimationNode;
import com.ostsoft.games.jsm.editor.animation.tree.AnimationRootNode;
import com.ostsoft.games.jsm.editor.animation.tree.FrameNode;
import com.ostsoft.games.jsm.editor.animation.tree.PartNode;
import com.ostsoft.games.jsm.editor.animation.tree.SpriteMapNode;
import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.observer.Observer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;

public class AnimationTree extends JTree implements Observer {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;
    private final AnimationRootNode rootNode;
    private boolean disableTreeEvents = false;

    public AnimationTree(EditorData editorData, AnimationPanelData animationPanelData) {
        super();
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;

        setComponentPopupMenu(new com.ostsoft.games.jsm.editor.common.gui.PopupMenu(editorData));
        setShowsRootHandles(true);
        setRootVisible(false);
//        setDragEnabled(true);
//        setTransferHandler(new TreeItemTransferHandler());
//        setDropMode(DropMode.INSERT);
        addTreeSelectionListener(e -> {
            disableTreeEvents = true;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    getLastSelectedPathComponent();
            if (node instanceof AnimationNode) {
                animationPanelData.setAnimation(((AnimationNode) node).getAnimation());
                editorData.fireEvent(EventType.ANIMATION);
            }
            else if (node instanceof FrameNode) {
                animationPanelData.setAnimationFrame(((FrameNode) node).getAnimationFrame());
                editorData.fireEvent(EventType.ANIMATION_FRAME);
            }
            else if (node instanceof PartNode && node.getParent() instanceof FrameNode) {
                animationPanelData.setAnimationFrame(((FrameNode) node.getParent()).getAnimationFrame());
                editorData.fireEvent(EventType.ANIMATION_FRAME);
            }
            else if (node instanceof SpriteMapNode) {
                AnimationFrame animationFrame = null;
                if (node.getParent() instanceof FrameNode) {
                    animationFrame = ((FrameNode) node.getParent()).getAnimationFrame();
                }
                else if (node.getParent() instanceof PartNode) {
                    if (node.getParent().getParent() instanceof FrameNode) {
                        animationFrame = ((FrameNode) node.getParent().getParent()).getAnimationFrame();
                    }
                }

                if (animationFrame != null && animationFrame != animationPanelData.getAnimationFrame()) {
                    animationPanelData.setAnimationFrame(animationFrame);
                    editorData.fireEvent(EventType.ANIMATION_FRAME);
                }

                animationPanelData.setSelectEntry(((SpriteMapNode) node).getSpriteMapEntry());
                editorData.fireEvent(EventType.SPRITE_SELECTED);
            }


            disableTreeEvents = false;
        });


        rootNode = new AnimationRootNode(editorData, animationPanelData);
        setModel(new DefaultTreeModel(rootNode));
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        if (disableTreeEvents) {
            return;
        }
        if (eventType == EventType.ANIMATION) {
            rootNode.updateAnimation();
            ((DefaultTreeModel) getModel()).reload(rootNode);
        }
        else if (eventType == EventType.SPRITE_SELECTED) {
            if (animationPanelData.getSelectEntry() == null || animationPanelData.getAnimationFrame() == null) {
                return;
            }

            DefaultMutableTreeNode node;
            FrameNode frameNode = null;
            Enumeration e = rootNode.breadthFirstEnumeration();
            while (e.hasMoreElements()) {
                node = (DefaultMutableTreeNode) e.nextElement();
                if ((node instanceof FrameNode)) {
                    if (((FrameNode) node).getAnimationFrame() == animationPanelData.getAnimationFrame()) {
                        frameNode = ((FrameNode) node);
                        break;
                    }
                }
            }


            if (frameNode == null) {
                return;
            }

            e = frameNode.breadthFirstEnumeration();
            while (e.hasMoreElements()) {
                node = (DefaultMutableTreeNode) e.nextElement();
                if ((node instanceof SpriteMapNode)) {
                    if (((SpriteMapNode) node).getSpriteMapEntry() == animationPanelData.getSelectEntry()) {
                        TreeNode[] nodes = ((DefaultTreeModel) getModel()).getPathToRoot(node);
                        setExpandsSelectedPaths(true);
                        setSelectionPath(new TreePath(nodes));
                        return;
                    }
                }
            }
        }
    }
}
