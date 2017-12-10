package com.ostsoft.games.jsm.editor.animation;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.animation.AnimationFrame;
import com.ostsoft.games.jsm.animation.SpriteMapEntry;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PaletteData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.palette.Palette;

public class AnimationPanelData extends PanelData implements PaletteData {
    private Animation animation = null;
    private AnimationFrame animationFrame = null;
    private SpriteMapEntry selectEntry = null;

    private Palette palette;

    public AnimationPanelData(EditorData editorData) {
        super(editorData);
        setScale(editorData.getOptions().getAnimationScale());

    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public AnimationFrame getAnimationFrame() {
        return animationFrame;
    }

    public void setAnimationFrame(AnimationFrame animationFrame) {
        this.animationFrame = animationFrame;
    }

    public SpriteMapEntry getSelectEntry() {
        return selectEntry;
    }

    public void setSelectEntry(SpriteMapEntry selectEntry) {
        this.selectEntry = selectEntry;
    }

    @Override
    public Palette getPalette() {
        return palette;
    }

    @Override
    public void setPalette(Palette palette) {
        this.palette = palette;
    }

}
