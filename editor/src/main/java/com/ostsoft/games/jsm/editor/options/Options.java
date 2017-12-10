package com.ostsoft.games.jsm.editor.options;

import com.ostsoft.games.jsm.editor.common.util.ErrorUtil;
import com.ostsoft.games.jsm.editor.common.util.INIUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Options {
    private final String fileName = "jsm.ini";
    private Properties properties;

    public void load() {
        if (!new File(fileName).exists()) {
            properties = new Properties();
            return;
        }

        try {
            FileInputStream stream = new FileInputStream(fileName);
            properties = INIUtil.loadINI(stream);
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
            properties = new Properties();
        }
    }

    public void save() {
        try {
            FileOutputStream stream = new FileOutputStream(fileName);
            INIUtil.saveINI(stream, properties);
        } catch (IOException e) {
            ErrorUtil.displayStackTrace(e);
        }
    }


    public float getDefaultScale() {
        return Float.parseFloat(properties.getProperty("defaultScale", "1"));
    }

    public void setDefaultScale(float defaultScale) {
        if (defaultScale > 0) {
            properties.setProperty("defaultScale", String.valueOf(defaultScale));
        }
    }

    public float getZoomStep() {
        return Float.parseFloat(properties.getProperty("zoomStep", "0.2"));
    }

    public void setZoomStep(float step) {
        if (step > 0) {
            properties.setProperty("zoomStep", String.valueOf(step));
        }
    }

    public float getZoomWheelStep() {
        return Float.parseFloat(properties.getProperty("zoomWheelStep", "0.25"));
    }

    public void setZoomWheelStep(float scale) {
        if (scale > 0 || scale < 0) {
            properties.setProperty("zoomWheelStep", String.valueOf(scale));
        }
    }

    public boolean isOpenLastFile() {
        return Boolean.parseBoolean(properties.getProperty("openLastFile", "true"));
    }

    public void setOpenLastFile(boolean openLastFile) {
        properties.setProperty("openLastFile", String.valueOf(openLastFile));
    }

    public String getLastFile() {
        return properties.getProperty("lastFile", "");
    }

    public void setLastFile(String lastFile) {
        properties.setProperty("lastFile", lastFile);
    }

    public boolean isPointerSnes() {
        return Boolean.parseBoolean(properties.getProperty("pointerSnes", "true"));
    }

    public void setPointerSnes(boolean value) {
        properties.setProperty("pointerSnes", String.valueOf(value));
    }

    public int getPointerBase() {
        return Integer.parseInt(properties.getProperty("pointerBase", "16"));
    }

    public void setPointerBase(int value) {
        properties.setProperty("pointerBase", String.valueOf(value));
    }

    public int getNumberBase() {
        return Integer.parseInt(properties.getProperty("numberBase", "10"));
    }

    public void setNumberBase(int value) {
        properties.setProperty("numberBase", String.valueOf(value));
    }


    /* Animation */
    public boolean isCustomAnimationScale() {
        return Boolean.parseBoolean(properties.getProperty("customAnimationScale", "false"));
    }

    public void setCustomAnimationScale(boolean value) {
        properties.setProperty("customAnimationScale", String.valueOf(value));
    }

    public float getAnimationScale() {
        if (isCustomAnimationScale()) {
            return Float.parseFloat(properties.getProperty("animationScale", "1"));
        }
        else {
            return getDefaultScale();
        }
    }

    public void setAnimationScale(float scale) {
        if (scale > 0) {
            properties.setProperty("animationScale", String.valueOf(scale));
        }
    }


    /* Credits */
    public boolean isCustomCreditsScale() {
        return Boolean.parseBoolean(properties.getProperty("customCreditsScale", "false"));
    }

    public void setCustomCreditsScale(boolean value) {
        properties.setProperty("customCreditsScale", String.valueOf(value));
    }

    public float getCreditsScale() {
        if (isCustomCreditsScale()) {
            return Float.parseFloat(properties.getProperty("creditsScale", "1"));
        }
        else {
            return getDefaultScale();
        }
    }

    public void setCreditsScale(float scale) {
        if (scale > 0) {
            properties.setProperty("creditsScale", String.valueOf(scale));
        }
    }


    public boolean isReloadFileOnSave() {
        return Boolean.parseBoolean(properties.getProperty("reloadFileOnSave", "true"));
    }

    public void setReloadFileOnSave(boolean reloadFileOnSave) {
        properties.setProperty("reloadFileOnSave", String.valueOf(reloadFileOnSave));
    }
}
