package com.ostsoft.games.jsm.editor.common;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.FutureTask;

public class Progress extends JFrame implements Runnable {
    public final LinkedList<FutureTask> tasks;
    public final int numberOfTasks;
    private final JProgressBar progressBar;
    public FutureTask currentTask;

    public Progress(Collection<FutureTask> tasks) {
        this.tasks = new LinkedList<>(tasks);
        this.numberOfTasks = tasks.size();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setType(Type.UTILITY);
        getContentPane().setLayout(new BorderLayout(0, 0));
        setTitle("jSM - Loading");
        setSize(640, 75);

//        JLabel lblJsm = new JLabel("jSM");
//        lblJsm.setFont(new Font("SansSerif", Font.PLAIN, 50));
//        lblJsm.setHorizontalAlignment(SwingConstants.CENTER);
//        getContentPane().add(lblJsm, BorderLayout.NORTH);
//
        JLabel label = new JLabel("Loading...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        if (numberOfTasks <= 1) {
            progressBar.setIndeterminate(true);
        }
        else {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
        }
        getContentPane().add(progressBar, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void run() {
        while (!tasks.isEmpty() || currentTask != null) {
            if (currentTask != null) {
                if (currentTask.isDone()) {
                    currentTask = null;
                }
            }
            else {
                currentTask = tasks.pop();
                new Thread(currentTask).run();
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(((numberOfTasks - tasks.size()) * 100) / numberOfTasks);
                });
            }


//            try {
//                Thread.sleep(10);
//            }
//            catch (InterruptedException ignored) {
//            }
        }
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(100);
            SwingUtilities.invokeLater(this::dispose);
        });

    }


}
