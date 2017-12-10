package com.ostsoft.games.jsm.editor.common.util;

import javax.swing.*;

public class ErrorUtil {
    private ErrorUtil() {

    }

    public static void displayStackTrace(Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        e.printStackTrace();
    }

    public static void displayErrorBox(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
