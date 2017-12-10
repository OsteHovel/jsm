package com.ostsoft.games.jsm.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public final class ConsoleUtil {
    private final static List<ConsoleObserver> consoleObserverList = new ArrayList<>();

    public static void writeToConsole(String string) {
        Calendar calendar = Calendar.getInstance();
        String timestamp = new SimpleDateFormat("[HH:mm:ss] ").format(calendar.getTime());
        String s = timestamp + string;
        System.out.println(s);
        for (ConsoleObserver consoleObserver : consoleObserverList) {
            consoleObserver.consoleEvent(s);
        }
    }

    public static void writeToConsole(String[] string) {
        ConsoleUtil.writeToConsole(Arrays.toString(string));
    }

    public static void writeToConsole(boolean b) {
        writeToConsole(b + "");
    }

    public static void writeToConsole(int i) {
        writeToConsole(i + "");
    }

    public static void writeToConsole(float f) {
        writeToConsole(f + "");
    }

    public static void writeToConsole(double d) {
        writeToConsole(d + "");
    }

    public static void writeToConsole() {
        writeToConsole("");
    }

    public static synchronized void registerObserver(ConsoleObserver observer) {
        consoleObserverList.add(observer);
    }

    public static synchronized void removeObserver(ConsoleObserver observer) {
        consoleObserverList.remove(observer);
    }
}
