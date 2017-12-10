package com.ostsoft.games.jsm.editor.tilemap;

import com.ostsoft.games.jsm.common.ImageSupport;
import com.ostsoft.games.jsm.common.ImageWithPalette;
import com.ostsoft.games.jsm.common.Tile;
import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.observer.command.ChangeTileBytes;
import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.editor.common.util.ResourceUtil;
import com.ostsoft.games.jsm.palette.DnDColor;
import com.ostsoft.games.jsm.palette.Palette;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileUtil {

    public static void importTiles(EditorData editorData, List<DnDColor> colors, List<Tile> tiles) {
        List<FileNameExtensionFilter> extensionFilters = new ArrayList<>();
        extensionFilters.add(new FileNameExtensionFilter("Tiles (*.gfx, *.png)", "gfx", "png"));
        extensionFilters.add(new FileNameExtensionFilter("Raw (.gfx)", "gfx"));
        FileNameExtensionFilter png = new FileNameExtensionFilter("PNG image (.png)", "png");
        extensionFilters.add(png);

        FileChooserUtil.FileDialogReturn dialog = FileChooserUtil.openDialog(extensionFilters, "", true);
        if (dialog == null) {
            return;
        }

        try {
            String extension = FileChooserUtil.getExtension(dialog.file);
            if (extension.equals("png") || dialog.fileFilter == png) {
                BufferedImage image = ImageIO.read(dialog.file);
                importTilesFromImage(editorData, image, colors, tiles);
            }
            else {
                byte[] bytes = ResourceUtil.getBytes(new FileInputStream(dialog.file));
                int i = 0;
                UUID uuid = UUID.randomUUID();

                for (Tile tile : tiles) {
                    byte[] tileBytes = new byte[32];
                    if (i + tileBytes.length >= bytes.length) {
                        break;
                    }

                    System.arraycopy(bytes, i, tileBytes, 0, tileBytes.length);
                    editorData.getCommandCenter().executeCommand(new ChangeTileBytes(uuid, editorData, tile, tileBytes));
                    i += tileBytes.length;
                }
            }
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }
    }

    public static void importTilesFromImage(EditorData editorData, BufferedImage image, List<DnDColor> colors, List<Tile> tiles) {
        UUID uuid = UUID.randomUUID();

        int cols = image.getWidth() / 8;
        int rows = image.getHeight() / 8;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Tile tile = tiles.get(row * cols + col);
                int[][] ints = new int[8][8];
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int rgb = image.getRGB(col * 8 + x, row * 8 + y);
                        for (DnDColor dnDColor : colors) {
                            if (dnDColor.getRGB() == rgb) {
                                // Found color of pixel ;-)
                                ints[x][y] = colors.indexOf(dnDColor);
                                break;
                            }
                        }
                    }
                }
                byte[] bytes = ImageUtil.getBytes(ints, 4);
                editorData.getCommandCenter().executeCommand(new ChangeTileBytes(uuid, editorData, tile, bytes));
            }
        }
    }


    public static void exportTiles(List<Tile> tiles, Palette palette) {
        List<FileNameExtensionFilter> extensionFilters = new ArrayList<>();
        extensionFilters.add(new FileNameExtensionFilter("Raw (.gfx)", "gfx"));
        FileNameExtensionFilter png = new FileNameExtensionFilter("PNG image (.png)", "png");
        extensionFilters.add(png);

        FileChooserUtil.FileDialogReturn fileDialogReturn = FileChooserUtil.saveDialog(extensionFilters, "tiles", false);
        if (fileDialogReturn == null) {
            return;
        }

        try {
            File selectedFile = fileDialogReturn.file;
            FileOutputStream outputStream = new FileOutputStream(selectedFile);

            if (fileDialogReturn.fileFilter == png) {
                BufferedImage image = exportTilesAsImage(tiles, palette);
                ImageIO.write(image, "png", outputStream);
            }
            else {
                int size = 0;
                for (Tile tile : tiles) {
                    size += tile.getBytes().length;
                }
                byte[] bytes = new byte[size];
                int dstPos = 0;
                for (Tile tile : tiles) {
                    System.arraycopy(tile.getBytes(), 0, bytes, dstPos, tile.getBytes().length);
                    dstPos += tile.getBytes().length;
                }
                outputStream.write(bytes);
            }
            outputStream.close();
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }

    }

    public static BufferedImage exportTilesAsImage(List<Tile> tiles, Palette palette) {
        int columns = 16;
        int width = columns * 8;
        int height = ((tiles.size() / columns) * 8);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, width, height);

        int x = 0;
        int y = 0;
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (tile instanceof ImageSupport) {
                g.drawImage(((ImageSupport) tile).getImage(), x, y, null);
            }
            else if (tile instanceof ImageWithPalette) {
                g.drawImage(((ImageWithPalette) tile).getImage(palette), x, y, null);
            }
            x += 8;
            if (x / 8 >= columns) {
                x = 0;
                y += 8;
            }
        }
        g.dispose();

        return image;
    }
}