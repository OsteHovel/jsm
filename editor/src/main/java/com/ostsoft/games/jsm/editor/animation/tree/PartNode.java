package com.ostsoft.games.jsm.editor.animation.tree;

import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class PartNode extends DefaultMutableTreeNode {
    private final EditorData editorData;
    private final AnimationPanelData animationPanelData;
    private final String name;

    public PartNode(EditorData editorData, AnimationPanelData animationPanelData, List<SpriteMapEntry> spriteMapEntries, String name) {
        this.editorData = editorData;
        this.animationPanelData = animationPanelData;
        this.name = name;

        for (SpriteMapEntry spriteMapEntry : spriteMapEntries) {
            SpriteMapNode spriteMapNode = new SpriteMapNode(editorData, animationPanelData, spriteMapEntry);
            add(spriteMapNode);
        }

    }

    @Override
    public String toString() {
        return name;
    }
}
