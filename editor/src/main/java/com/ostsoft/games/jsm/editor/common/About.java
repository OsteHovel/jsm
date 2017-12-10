package com.ostsoft.games.jsm.editor.common;

import com.ostsoft.games.jsm.editor.common.util.ResourceUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class About extends JFrame {
    public final static String VERSION = "0.12";
    public static final String SPECIAL = "WIP";
    public static final boolean DEBUG = true;

    public About() throws HeadlessException {
        super();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(640, 480);
        setTitle("About jSM " + VERSION);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        Box verticalBox = Box.createVerticalBox();
        panel.add(verticalBox, BorderLayout.NORTH);

        JLabel lblJsm = new JLabel("jSM");
        lblJsm.setFont(new Font("SansSerif", Font.PLAIN, 26));
        lblJsm.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblJsm.setPreferredSize(new Dimension(100, 40));
        lblJsm.setMaximumSize(new Dimension(100, 40));
        lblJsm.setHorizontalTextPosition(SwingConstants.CENTER);
        lblJsm.setHorizontalAlignment(SwingConstants.CENTER);
        verticalBox.add(lblJsm);

        JLabel lblBy = new JLabel("by");
        lblBy.setFont(new Font("SansSerif", Font.PLAIN, 14));
        verticalBox.add(lblBy);
        lblBy.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBy.setPreferredSize(new Dimension(100, 20));
        lblBy.setMaximumSize(new Dimension(100, 20));
        lblBy.setHorizontalTextPosition(SwingConstants.CENTER);
        lblBy.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblOsteHovel = new JLabel("Oste Hovel");
        lblOsteHovel.setHorizontalTextPosition(SwingConstants.CENTER);
        lblOsteHovel.setHorizontalAlignment(SwingConstants.CENTER);
        lblOsteHovel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblOsteHovel.setPreferredSize(new Dimension(200, 25));
        lblOsteHovel.setMinimumSize(new Dimension(61, 25));
        lblOsteHovel.setMaximumSize(new Dimension(200, 25));
        lblOsteHovel.setAlignmentX(Component.CENTER_ALIGNMENT);
        verticalBox.add(lblOsteHovel);

        CloseAction closeAction = new CloseAction();
        JButton btnClose = new JButton(closeAction);

        JTextArea txtpnAsf = new JTextArea();
        txtpnAsf.setWrapStyleWord(true);
        txtpnAsf.setLineWrap(true);
        txtpnAsf.setEditable(false);
        txtpnAsf.setText(ResourceUtil.loadResourceAsString("doc/thanks.txt") + "\n\n" + ResourceUtil.loadResourceAsString("doc/changelog.txt") + "\n\n" + ResourceUtil.loadResourceAsString("doc/licences.txt"));
        txtpnAsf.setCaretPosition(0);

        JScrollPane comp = new JScrollPane(txtpnAsf);
        panel.add(comp, BorderLayout.CENTER);
        getRootPane().setDefaultButton(btnClose);
        btnClose.setMnemonic(KeyEvent.VK_ENTER);
        panel.add(btnClose, BorderLayout.SOUTH);


        setVisible(true);

        getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeAction");
        getRootPane().getActionMap().put("closeAction", closeAction);

        txtpnAsf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeAction");
        txtpnAsf.getActionMap().put("closeAction", closeAction);

    }


    private class CloseAction extends AbstractAction {
        public CloseAction() {
            putValue(NAME, "Close");
        }

        public void actionPerformed(ActionEvent e) {
            dispatchEvent(new WindowEvent(About.this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
