package com.ostsoft.games.jsm.editor.options;

import javax.swing.*;
import java.awt.*;

public class GeneralPane extends OptionsPane {
    private final JCheckBox chckbxOpenLastFile = new JCheckBox("Open last file on start");
    private final JLabel lbLastFile = new JLabel("Last: ");
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ButtonGroup buttonGroup_1 = new ButtonGroup();
    private final ButtonGroup buttonGroup_2 = new ButtonGroup();
    private final JLabel lblZoomStep = new JLabel("Zoom step");
    private final JLabel lblZoomWheelStep = new JLabel("Zoom Wheel step");
    private final JTextField tfZoomStep = new JTextField();
    private final JTextField tfZoomWheelStep = new JTextField();
    private final JCheckBox chckbxReloadFileOn = new JCheckBox("Reload file on save (not working)");
    private JTextField tfDefaultScale = new JTextField();

    public GeneralPane() {
        super("General");
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{5, 0, 0, 173, 5, 0};
        gridBagLayout.rowHeights = new int[]{5, 0, 0, 0, 15, 0, 0, 15, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JLabel lblDefaultScale = new JLabel("Default scale");
        GridBagConstraints gbc_lblDefaultScale = new GridBagConstraints();
        gbc_lblDefaultScale.anchor = GridBagConstraints.EAST;
        gbc_lblDefaultScale.insets = new Insets(0, 0, 5, 5);
        gbc_lblDefaultScale.gridx = 1;
        gbc_lblDefaultScale.gridy = 1;
        add(lblDefaultScale, gbc_lblDefaultScale);

        GridBagConstraints gbc_tfDefaultScale = new GridBagConstraints();
        gbc_tfDefaultScale.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfDefaultScale.insets = new Insets(0, 0, 5, 5);
        gbc_tfDefaultScale.gridx = 2;
        gbc_tfDefaultScale.gridy = 1;
        add(tfDefaultScale, gbc_tfDefaultScale);
        tfDefaultScale.setColumns(10);

        GridBagConstraints gbc_lblZoomStep = new GridBagConstraints();
        gbc_lblZoomStep.anchor = GridBagConstraints.EAST;
        gbc_lblZoomStep.insets = new Insets(0, 0, 5, 5);
        gbc_lblZoomStep.gridx = 1;
        gbc_lblZoomStep.gridy = 2;
        add(lblZoomStep, gbc_lblZoomStep);

        GridBagConstraints gbc_tfZoomStep = new GridBagConstraints();
        gbc_tfZoomStep.insets = new Insets(0, 0, 5, 5);
        gbc_tfZoomStep.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfZoomStep.gridx = 2;
        gbc_tfZoomStep.gridy = 2;
        tfZoomStep.setColumns(10);
        add(tfZoomStep, gbc_tfZoomStep);

        GridBagConstraints gbc_lblZoomWheelStep = new GridBagConstraints();
        gbc_lblZoomWheelStep.anchor = GridBagConstraints.EAST;
        gbc_lblZoomWheelStep.insets = new Insets(0, 0, 5, 5);
        gbc_lblZoomWheelStep.gridx = 1;
        gbc_lblZoomWheelStep.gridy = 3;
        add(lblZoomWheelStep, gbc_lblZoomWheelStep);

        GridBagConstraints gbc_tfZoomWheelStep = new GridBagConstraints();
        gbc_tfZoomWheelStep.insets = new Insets(0, 0, 5, 5);
        gbc_tfZoomWheelStep.fill = GridBagConstraints.HORIZONTAL;
        gbc_tfZoomWheelStep.gridx = 2;
        gbc_tfZoomWheelStep.gridy = 3;
        tfZoomWheelStep.setColumns(10);
        add(tfZoomWheelStep, gbc_tfZoomWheelStep);

        GridBagConstraints gbc_chckbxOpenLastFile = new GridBagConstraints();
        gbc_chckbxOpenLastFile.gridwidth = 2;
        gbc_chckbxOpenLastFile.anchor = GridBagConstraints.WEST;
        gbc_chckbxOpenLastFile.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxOpenLastFile.gridx = 2;
        gbc_chckbxOpenLastFile.gridy = 5;
        add(chckbxOpenLastFile, gbc_chckbxOpenLastFile);

        GridBagConstraints gbc_lbLastFile = new GridBagConstraints();
        gbc_lbLastFile.anchor = GridBagConstraints.WEST;
        gbc_lbLastFile.gridwidth = 3;
        gbc_lbLastFile.insets = new Insets(0, 0, 5, 5);
        gbc_lbLastFile.gridx = 1;
        gbc_lbLastFile.gridy = 6;
        add(lbLastFile, gbc_lbLastFile);

        GridBagConstraints gbc_chckbxReloadFileOn = new GridBagConstraints();
        gbc_chckbxReloadFileOn.anchor = GridBagConstraints.WEST;
        gbc_chckbxReloadFileOn.gridwidth = 2;
        gbc_chckbxReloadFileOn.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxReloadFileOn.gridx = 2;
        gbc_chckbxReloadFileOn.gridy = 7;
        add(chckbxReloadFileOn, gbc_chckbxReloadFileOn);
    }

    @Override
    public void load(Options options) {
        tfDefaultScale.setText(String.valueOf(options.getDefaultScale() * 100));
        tfZoomStep.setText(String.valueOf(options.getZoomStep() * 100));
        tfZoomWheelStep.setText(String.valueOf(options.getZoomWheelStep() * 100));

        chckbxOpenLastFile.setSelected(options.isOpenLastFile());
        lbLastFile.setText("Last: " + options.getLastFile());
        chckbxReloadFileOn.setSelected(options.isReloadFileOnSave());


    }

    @Override
    public void save(Options options) {
        options.setDefaultScale(Float.parseFloat(tfDefaultScale.getText()) / 100);
        options.setZoomStep(Float.parseFloat(tfZoomStep.getText()) / 100);
        options.setZoomWheelStep(Float.parseFloat(tfZoomWheelStep.getText()) / 100);
        options.setOpenLastFile(chckbxOpenLastFile.isSelected());
        options.setReloadFileOnSave(chckbxReloadFileOn.isSelected());

    }
}
