package com.ostsoft.games.jsm.editor.options;

import javax.swing.*;
import java.awt.*;

public class CreditsPane extends OptionsPane {
    private final JCheckBox chckbxScale = new JCheckBox("Use custom credits scale");
    private JTextField tfScale = new JTextField();

    public CreditsPane() {
        super("Credits");
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{5, 0, 0, 173, 5, 0};
        gridBagLayout.rowHeights = new int[]{5, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        GridBagConstraints gbc_chckbxDefaultScale = new GridBagConstraints();
        gbc_chckbxDefaultScale.gridwidth = 2;
        gbc_chckbxDefaultScale.anchor = GridBagConstraints.WEST;
        gbc_chckbxDefaultScale.insets = new Insets(0, 0, 5, 5);
        gbc_chckbxDefaultScale.gridx = 2;
        gbc_chckbxDefaultScale.gridy = 1;
        add(chckbxScale, gbc_chckbxDefaultScale);


        JLabel lblDefaultScale = new JLabel("Credits scale");
        GridBagConstraints gbc_lblDefaultScale = new GridBagConstraints();
        gbc_lblDefaultScale.anchor = GridBagConstraints.EAST;
        gbc_lblDefaultScale.insets = new Insets(0, 0, 5, 5);
        gbc_lblDefaultScale.gridx = 1;
        gbc_lblDefaultScale.gridy = 2;
        add(lblDefaultScale, gbc_lblDefaultScale);

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.gridwidth = 2;
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.gridx = 2;
        gbc_textField.gridy = 2;
        add(tfScale, gbc_textField);
        tfScale.setColumns(10);

        chckbxScale.addActionListener(e -> tfScale.setEnabled(chckbxScale.isSelected()));
    }

    @Override
    public void load(Options options) {
        chckbxScale.setSelected(options.isCustomCreditsScale());
        tfScale.setText(String.valueOf(options.getCreditsScale() * 100));

        tfScale.setEnabled(chckbxScale.isSelected());
    }

    @Override
    public void save(Options options) {
        options.setCustomCreditsScale(chckbxScale.isSelected());
        options.setCreditsScale(Float.parseFloat(tfScale.getText()) / 100);
    }
}
