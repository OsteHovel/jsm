package com.ostsoft.games.jsm.editor;

import com.ostsoft.games.jsm.editor.common.observer.EventType;
import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.common.util.FileChooserUtil;
import com.ostsoft.games.jsm.util.ByteStream;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

public class EditorFile {
    private final Editor editor;
    private File file = null; // Current or last opened file

    public EditorFile(Editor editor) {
        this.editor = editor;
    }

    public void load(boolean reload) {
        if (file == null || !reload) {
            String fileName = "sm.smc";
            if (file != null) {
                fileName = file.getAbsolutePath();
            }
            File fileFromOpenFileChooser = FileChooserUtil.openDialogSingle(fileName, true, "Super Metroid (*.smc, *.sfc, *.snes)", "smc", "sfc", "snes");
            if (fileFromOpenFileChooser != null) {
                load(fileFromOpenFileChooser);
            }
        }
        else {
            load(file);
        }

    }

    public void load(File file) {
        ByteStream byteStreamFromFile = getByteStreamFromFile(file);
        if (byteStreamFromFile != null) {
            this.file = file;
            editor.load(byteStreamFromFile);
            if (editor.getEditorData() != null) {
                editor.getEditorData().getOptions().setLastFile(file.getAbsolutePath());
            }
        }
    }

    private ByteStream getByteStreamFromFile(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            int len = stream.available();
            byte[] bytes = new byte[len];
            stream.read(bytes, 0, len);
            return new ByteStream(bytes);
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }
        return null;
    }


    public void save(boolean saveAs) {
        try {
            OutputStream outputStream;
            if (file != null && !saveAs) {
                outputStream = new FileOutputStream(file);
            }
            else {
                String fileName = "sm.smc";
                if (file != null) {
                    fileName = file.getAbsolutePath();
                }
                FileChooserUtil.FileDialogReturn fileDialogReturn = FileChooserUtil.saveDialog(Collections.singletonList(new FileNameExtensionFilter("Super Metroid (*.smc, *.sfc, *.snes)", "smc", "sfc", "snes")), fileName, true);
                if (fileDialogReturn == null) {
                    return;
                }
                File file = fileDialogReturn.file;
                if (file == null) {
                    return;
                }
                this.file = file;
                outputStream = new FileOutputStream(file);
            }

            outputStream.write(editor.getEditorData().getSuperMetroid().getStream().getBuffer());
            outputStream.close();
            editor.getEditorData().fireEvent(EventType.STATUS_BAR_MESSAGE, "Saved");
        } catch (IOException e1) {
            ErrorUtil.displayStackTrace(e1);
        }
    }
}
