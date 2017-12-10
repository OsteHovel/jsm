package com.ostsoft.games.jsm.editor.palette;

import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.editor.common.util.ResourceUtil;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaletteUtil {
    public static List<DnDColor> importPalette() {
        List<FileNameExtensionFilter> filters = new ArrayList<>();
        filters.add(new FileNameExtensionFilter("Palettes (*.bin, *.tpl, *.png)", "bin", "tpl", "png"));
        filters.add(new FileNameExtensionFilter("Raw (.bin)", "bin"));
        FileNameExtensionFilter tpl = new FileNameExtensionFilter("TPL-Compatible palette (.tpl)", "tpl");
        filters.add(tpl);
        FileNameExtensionFilter png = new FileNameExtensionFilter("PNG image (.png)", "png");
        filters.add(png);

        FileChooserUtil.FileDialogReturn dialog = FileChooserUtil.openDialog(filters, "", true);
        if (dialog == null) {
            return null;
        }

        try {
            String extension = FileChooserUtil.getExtension(dialog.file);
            if (extension.equals("tpl") || dialog.fileFilter == tpl) {
                byte[] bytes = ResourceUtil.getBytes(new FileInputStream(dialog.file));
                return com.ostsoft.games.jsm.palette.PaletteUtil.parseTPLCompatiblePalette(bytes);
            }
            else if (extension.equals("png") || dialog.fileFilter == png) {
                BufferedImage image = ImageIO.read(dialog.file);
                int cols = image.getWidth() / 16;
                int rows = image.getHeight() / 16;

                List<DnDColor> colors = new ArrayList<>(rows * 16);
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        int rgb = image.getRGB(col * 16 + 8, row * 16 + 8);
                        colors.add(new DnDColor(rgb));
                    }
                }
                return colors;
            }
            else {
                byte[] bytes = ResourceUtil.getBytes(new FileInputStream(dialog.file));
                return com.ostsoft.games.jsm.palette.PaletteUtil.decodePaletteToColors(bytes);
            }
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }
        return null;
    }

    public static void exportPalette(Palette palette) {
        FileNameExtensionFilter bin = new FileNameExtensionFilter("Raw (.bin)", "bin");
        FileNameExtensionFilter tpl = new FileNameExtensionFilter("TPL-Compatible palette (.tpl)", "tpl");
        FileNameExtensionFilter png = new FileNameExtensionFilter("PNG image (.png)", "png");
        List<FileNameExtensionFilter> filters = new ArrayList<>();
        filters.add(bin);
        filters.add(tpl);
        filters.add(png);

        FileChooserUtil.FileDialogReturn dialog = FileChooserUtil.saveDialog(filters, "palette", true);
        if (dialog == null) {
            return;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dialog.file);
            if (dialog.fileFilter == tpl) {
                fileOutputStream.write(com.ostsoft.games.jsm.palette.PaletteUtil.getTPLCompatiblePalette(palette));
            }
            else if (dialog.fileFilter == png) {
                int size = palette.getColors().size();
                int height = (size / 16) * 16;
                int width = 16 * 16;

                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) image.getGraphics();
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, width, height);
                for (int i = 0; i < size; i++) {
                    g.setColor(palette.getColors().get(i));
                    int y = (i / 16) * 16;
                    int x = (i - y) * 16;
                    g.fillRect(x, y, 16, 16);
                }

                ImageIO.write(image, "png", dialog.file);
            }
            else {
                fileOutputStream.write(com.ostsoft.games.jsm.palette.PaletteUtil.encodeColorsToPalette(palette.getColors()));
            }
            fileOutputStream.close();
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }
    }
}
