package com.ostsoft.games.jsm.editor.animation.tree;

import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;

import javax.swing.tree.DefaultMutableTreeNode;

public class SpriteMapNode extends DefaultMutableTreeNode {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;
    private final SpriteMapEntry spriteMapEntry;

    public SpriteMapNode(EditorData editorData, AnimationPanelData animationPanelData, SpriteMapEntry spriteMapEntry) {
        super("Sprite " + Integer.toHexString(editorData.getSuperMetroid().getSpriteMapManager().getOffset(spriteMapEntry)).toUpperCase());
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
        this.spriteMapEntry = spriteMapEntry;
    }

    public SpriteMapEntry getSpriteMapEntry() {
        return spriteMapEntry;
    }
}
