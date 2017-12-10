package com.ostsoft.games.jsm.editor.animation.tree;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;

import javax.swing.tree.DefaultMutableTreeNode;

public class AnimationNode extends DefaultMutableTreeNode {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;
    private final Animation animation;

    public AnimationNode(EditorData editorData, AnimationPanelData animationPanelData, Animation animation) {
        super("Animation");
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
        this.animation = animation;
        int i = 1;
        for (AnimationFrame animationFrame : animationPanelData.getAnimation().getAnimationFrames()) {
            add(new FrameNode(editorData, animationPanelData, animationFrame, "Frame " + i));
            i++;
        }

    }

    public Animation getAnimation() {
        return animation;
    }
}
