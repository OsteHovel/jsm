package com.ostsoft.games.jsm.editor.common.list;

import com.ostsoft.games.jsm.animation.Animation;
import com.ostsoft.games.jsm.common.ImageSupport;
import com.ostsoft.games.jsm.common.ImageWithPalette;
import com.ostsoft.games.jsm.credits.CreditLine;
import com.ostsoft.games.jsm.editor.animation.AnimationPanelData;
import com.ostsoft.games.jsm.editor.common.PaletteData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.projectile.Projectile;
import com.ostsoft.games.jsm.util.ImagePanel;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CustomListRenderer<T> implements ListCellRenderer<T> {
    private final JPanel panel = new JPanel(new BorderLayout());
    private final ImagePanel imagePanel = new ImagePanel();
    private final JLabel label = new JLabel();
    private final PanelData panelData;
    private final boolean scaleImage;
    private final boolean showLabel;
    private HashMap<Object, Image> imageMap = new HashMap<>();

    public CustomListRenderer(PanelData panelData, boolean scaleImage, boolean showLabel) {
        this.panelData = panelData;
        imagePanel.setBackground(null);
        panel.add(imagePanel, BorderLayout.CENTER);
        this.scaleImage = scaleImage;
        this.showLabel = showLabel;

        if (showLabel) {
            panel.add(label, BorderLayout.SOUTH);
        }
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends T> list, T value, int index,
            boolean isSelected, boolean cellHasFocus) {
        Image image = imageMap.get(value);
        if (image == null || isSelected) {
            if (value instanceof CreditLine) {
                image = ((CreditLine) value).getImage();
            }
            else if (value instanceof Image) {
                image = (Image) value;
            }
            else if (value instanceof ImageIcon) {
                image = ((ImageIcon) value).getImage();
            }
            else if (value instanceof ImageSupport) {
                image = ((ImageSupport) value).getImage();
            }
            else if (value instanceof ImageWithPalette && panelData instanceof PaletteData && ((PaletteData) panelData).getPalette() != null) {
                image = ((ImageWithPalette) value).getImage(((PaletteData) panelData).getPalette());
            }
            else if (value instanceof Color) {
                image = ImageUtil.get8x8((Color) value);
            }
            else if (value instanceof Animation && panelData instanceof AnimationPanelData && ((Animation) value).getAnimationFrames().size() > 0 && ((AnimationPanelData) panelData).getPalette() != null) {
                image = ((Animation) value).getAnimationFrames().get(0).getImage(((AnimationPanelData) panelData).getPalette().getColors());
            }
            else if (value instanceof Projectile && panelData instanceof AnimationPanelData && ((Projectile) value).projectileAnimations[0].getAnimationFrames().size() > 0 && ((AnimationPanelData) panelData).getPalette() != null) {
                image = ((Projectile) value).projectileAnimations[0].getAnimationFrames().get(0).getImage(((AnimationPanelData) panelData).getPalette().getColors());
            }
            else {
                image = ImageUtil.get8x8(Color.RED);
            }

            if (image != null) {
                if (scaleImage) {
                    int width = (int) (image.getWidth(null) * panelData.getScale());
                    if (width < 4) {
                        width = 4;
                    }
                    int height = (int) (image.getHeight(null) * panelData.getScale());
                    if (height < 4) {
                        height = 4;
                    }

                    image = image.getScaledInstance(
                            width,
                            height,
                            Image.SCALE_FAST
                    );
                }

                imageMap.put(value, image);
            }
        }

        if (isSelected) {
            imagePanel.setForeground(list.getSelectionForeground());
        }
        else {
            imagePanel.setForeground(null);
        }

        imagePanel.setImage(image);
        if (showLabel) {
            label.setText(value.toString());
        }

        panel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        panel.setForeground(SystemColor.textHighlight);

        return panel;
    }

    public void clearCache() {
        imageMap.clear();
    }
}
