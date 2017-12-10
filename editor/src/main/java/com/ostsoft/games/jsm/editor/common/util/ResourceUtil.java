package com.ostsoft.games.jsm.editor.common.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ResourceUtil {

    public static byte[] getBytes(InputStream stream) throws IOException {
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        stream.close();

        return bytes;
    }

    public static String loadResourceAsString(String fileName) {
        InputStream inputStream = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            inputStream = getInputStream(fileName);
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("[ResourceUtil] Could not read file: " + e.getMessage());
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return sb.toString();
    }

    public static InputStream getInputStream(String fileName) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
    }

    public static Icon getIcon(String iconName) {
        URL resource = ClassLoader.getSystemClassLoader().getResource(iconName);
        if (resource == null) {
            return null;
        }
        try {
            return new ImageIcon(ImageIO.read(resource).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
