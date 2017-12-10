package com.ostsoft.games.jsm.editor.common.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

public class FileChooserUtil {

    private FileChooserUtil() {
    }

    public static File openDialogSingle(String defaultName, boolean acceptAll, String description, String... extensions) {
        FileDialogReturn fileDialogReturn = FileChooserUtil.openDialog(
                Collections.singletonList(new FileNameExtensionFilter(description, extensions))
                , defaultName
                , acceptAll);
        if (fileDialogReturn == null) {
            return null;
        }
        return fileDialogReturn.file;
    }

    public static File saveDialogSingle(String defaultName, boolean acceptAll, String description, String... extensions) {
        FileDialogReturn fileDialogReturn = FileChooserUtil.saveDialog(
                Collections.singletonList(new FileNameExtensionFilter(description, extensions))
                , defaultName
                , acceptAll);
        if (fileDialogReturn == null) {
            return null;
        }
        return fileDialogReturn.file;
    }

    public static void saveImageAsFile(RenderedImage image, String defaultName) {
        try {
            File file = saveDialogSingle(defaultName, true, "PNG image (.png)", "png");
            if (file == null) {
                return;
            }

            OutputStream outputStream = new FileOutputStream(file);
            ImageIO.write(image, "png", outputStream);
            outputStream.close();
        } catch (IOException e2) {
            ErrorUtil.displayStackTrace(e2);
        }
    }

    public static FileDialogReturn openDialog(List<FileNameExtensionFilter> fileNameExtensionFilters, String defaultName, boolean acceptAll) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(defaultName));
        if (fileNameExtensionFilters.size() > 0) {
            fileChooser.setFileFilter(fileNameExtensionFilters.get(0));
            for (FileNameExtensionFilter fileFilter : fileNameExtensionFilters) {
                fileChooser.addChoosableFileFilter(fileFilter);
            }
            fileChooser.setAcceptAllFileFilterUsed(acceptAll);
        }
        else {
            fileChooser.setAcceptAllFileFilterUsed(true);
        }

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                return new FileDialogReturn(file, fileChooser.getFileFilter());
            }
            else {
                JOptionPane.showMessageDialog(null, "No file selected");
            }
        }
        return null;
    }


    public static FileDialogReturn saveDialog(List<FileNameExtensionFilter> fileNameExtensionFilters, String defaultName, boolean acceptAll) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(defaultName));
        if (fileNameExtensionFilters.size() > 0) {
            fileChooser.setFileFilter(fileNameExtensionFilters.get(0));
            for (FileNameExtensionFilter fileFilter : fileNameExtensionFilters) {
                fileChooser.addChoosableFileFilter(fileFilter);
            }
            fileChooser.setAcceptAllFileFilterUsed(acceptAll);
        }
        else {
            fileChooser.setAcceptAllFileFilterUsed(true);
        }


        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                // Ask to overwrite
                int reply = JOptionPane.showConfirmDialog(null, "File " + file.getName() + "already exist in \n" + file.getParent() + "\n" + "Do you want to overwrite?", "Warning", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    return new FileDialogReturn(file, fileChooser.getFileFilter());
                }
            }
            else {
                // Add extension
                File fileWithExtension = getSelectedFileWithExtension(fileChooser);
                if (fileWithExtension.exists()) {
                    int reply = JOptionPane.showConfirmDialog(null, "File " + fileWithExtension.getName() + " already exist in \n" + fileWithExtension.getParent() + "\n" + "Do you want to overwrite?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        return new FileDialogReturn(fileWithExtension, fileChooser.getFileFilter());
                    }
                }
                else {
                    return new FileDialogReturn(fileWithExtension, fileChooser.getFileFilter());
                }
            }
        }
        return null;
    }

    /**
     * Returns the selected file from a JFileChooser, including the extension from
     * the file filter.
     */
    private static File getSelectedFileWithExtension(JFileChooser c) {
        File file = c.getSelectedFile();
        if (c.getFileFilter() instanceof FileNameExtensionFilter) {
            String[] exts = ((FileNameExtensionFilter) c.getFileFilter()).getExtensions();
            String nameLower = file.getName().toLowerCase();
            for (String ext : exts) { // check if it already has a valid extension
                if (nameLower.endsWith('.' + ext.toLowerCase())) {
                    return file; // if yes, return as-is
                }
            }
            // if not, append the first extension from the selected filter
            file = new File(file.toString() + '.' + exts[0]);
        }
        return file;
    }

    /*
    * Get the extension of a file.
    */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static class FileDialogReturn {
        public final File file;
        public final FileFilter fileFilter;

        FileDialogReturn(File file, FileFilter fileFilter) {
            this.file = file;
            this.fileFilter = fileFilter;
        }
    }

}
