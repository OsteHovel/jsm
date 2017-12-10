package com.ostsoft.games.jsm.editor.animation.tree;

import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.animation.samus.SamusFrame;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;

import javax.swing.tree.DefaultMutableTreeNode;

public class FrameNode extends DefaultMutableTreeNode {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;
    private final AnimationFrame animationFrame;
    private final String name;

    public FrameNode(EditorData editorData, AnimationPanelData animationPanelData, AnimationFrame animationFrame, String name) {
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
        this.animationFrame = animationFrame;
        this.name = name;

        if (animationFrame instanceof SamusFrame) {
            SamusFrame samusFrame = (SamusFrame) animationFrame;
            add(new PartNode(editorData, animationPanelData, samusFrame.getTopSprites(), "Top"));
            add(new PartNode(editorData, animationPanelData, samusFrame.getBottomSprites(), "Bottom"));
        }
        else {
            for (SpriteMapEntry spriteMapEntry : animationFrame.getSpriteMapEntries()) {
                SpriteMapNode spriteMapNode = new SpriteMapNode(editorData, animationPanelData, spriteMapEntry);
                add(spriteMapNode);
            }
        }
    }


    public AnimationFrame getAnimationFrame() {
        return animationFrame;
    }

    @Override
    public String toString() {
        return name;
    }
}
