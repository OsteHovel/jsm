package com.ostsoft.games.jsm.editor.options;

import javax.swing.*;
import java.awt.*;

public class AnimationPane extends OptionsPane {
    private final JCheckBox chckbxCustomScale = new JCheckBox("Use custom animation scale");
    private JTextField tfScale = new JTextField();

    public AnimationPane() {
        super("Animation");
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
        add(chckbxCustomScale, gbc_chckbxDefaultScale);


        JLabel lblDefaultScale = new JLabel("Animation scale");
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

        chckbxCustomScale.addActionListener(e -> tfScale.setEnabled(chckbxCustomScale.isSelected()));
    }

    @Override
    public void load(Options options) {
        chckbxCustomScale.setSelected(options.isCustomAnimationScale());
        tfScale.setText(String.valueOf(options.getAnimationScale() * 100));

        tfScale.setEnabled(chckbxCustomScale.isSelected());
    }

    @Override
    public void save(Options options) {
        options.setCustomAnimationScale(chckbxCustomScale.isSelected());
        options.setAnimationScale(Float.parseFloat(tfScale.getText()) / 100);
    }
}
