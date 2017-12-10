package com.ostsoft.games.jsm.editor.animation.tree;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class AnimationRootNode extends DefaultMutableTreeNode {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;

    public AnimationRootNode(EditorData editorData, AnimationPanelData animationPanelData) {
        super("Animations");
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
    }


    public void updateAnimation() {
        if (getChildCount() > 0) {
            TreeNode node = getChildAt(0);
            if (node instanceof AnimationNode) {
                AnimationNode animationNode = (AnimationNode) node;
                if (animationPanelData.getAnimation() == animationNode.getAnimation()) {
                    return;
                }
            }
        }

        this.removeAllChildren();

        if (animationPanelData.getAnimation() != null) {
            add(new AnimationNode(editorData, animationPanelData, animationPanelData.getAnimation()));
        }
    }
}
