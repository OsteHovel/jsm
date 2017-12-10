package com.ostsoft.games.jsm.editor.options;

import javax.swing.*;
import java.awt.*;

public class FormatPane extends OptionsPane {
    private final JLabel lblPointerType = new JLabel("Pointers in");
    private final JLabel lblNumberType = new JLabel("Numbers in");
    private final JLabel lblPointerType_1 = new JLabel("Pointer type");
    private final JRadioButton rdbtnTypePc = new JRadioButton("PC");
    private final JRadioButton rdbtnTypeSnes = new JRadioButton("SNES");
    private final JRadioButton rdbtnPointerHex = new JRadioButton("HEX");
    private final JRadioButton rdbtnPointerDecimal = new JRadioButton("Decimal");
    private final JRadioButton rdbtnNumberHex = new JRadioButton("HEX");
    private final JRadioButton rdbtnNumberDec = new JRadioButton("Decimal");
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ButtonGroup buttonGroup_1 = new ButtonGroup();
    private final ButtonGroup buttonGroup_2 = new ButtonGroup();

    public FormatPane() {
        super("Format");
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{5, 0, 0, 173, 5, 0};
        gridBagLayout.rowHeights = new int[]{5, 0, 0, 0, 15, 0, 0, 15, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        GridBagConstraints gbc_lblPointerType_1 = new GridBagConstraints();
        gbc_lblPointerType_1.anchor = GridBagConstraints.EAST;
        gbc_lblPointerType_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblPointerType_1.gridx = 1;
        gbc_lblPointerType_1.gridy = 2;
        add(lblPointerType_1, gbc_lblPointerType_1);

        GridBagConstraints gbc_rdbtnTypeSnes = new GridBagConstraints();
        gbc_rdbtnTypeSnes.anchor = GridBagConstraints.WEST;
        gbc_rdbtnTypeSnes.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnTypeSnes.gridx = 2;
        gbc_rdbtnTypeSnes.gridy = 2;
        buttonGroup_1.add(rdbtnTypeSnes);
        add(rdbtnTypeSnes, gbc_rdbtnTypeSnes);

        GridBagConstraints gbc_rdbtnTypePc = new GridBagConstraints();
        gbc_rdbtnTypePc.anchor = GridBagConstraints.WEST;
        gbc_rdbtnTypePc.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnTypePc.gridx = 3;
        gbc_rdbtnTypePc.gridy = 2;
        buttonGroup_1.add(rdbtnTypePc);
        add(rdbtnTypePc, gbc_rdbtnTypePc);

        GridBagConstraints gbc_lblPointerType = new GridBagConstraints();
        gbc_lblPointerType.insets = new Insets(0, 0, 5, 5);
        gbc_lblPointerType.anchor = GridBagConstraints.EAST;
        gbc_lblPointerType.gridx = 1;
        gbc_lblPointerType.gridy = 3;
        add(lblPointerType, gbc_lblPointerType);

        GridBagConstraints gbc_rdbtnPointerHex = new GridBagConstraints();
        gbc_rdbtnPointerHex.anchor = GridBagConstraints.WEST;
        gbc_rdbtnPointerHex.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnPointerHex.gridx = 2;
        gbc_rdbtnPointerHex.gridy = 3;
        buttonGroup.add(rdbtnPointerHex);
        add(rdbtnPointerHex, gbc_rdbtnPointerHex);

        GridBagConstraints gbc_rdbtnPointerDecimal = new GridBagConstraints();
        gbc_rdbtnPointerDecimal.anchor = GridBagConstraints.WEST;
        gbc_rdbtnPointerDecimal.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnPointerDecimal.gridx = 3;
        gbc_rdbtnPointerDecimal.gridy = 3;
        buttonGroup.add(rdbtnPointerDecimal);
        add(rdbtnPointerDecimal, gbc_rdbtnPointerDecimal);

        GridBagConstraints gbc_lblNumberType = new GridBagConstraints();
        gbc_lblNumberType.insets = new Insets(0, 0, 5, 5);
        gbc_lblNumberType.anchor = GridBagConstraints.EAST;
        gbc_lblNumberType.gridx = 1;
        gbc_lblNumberType.gridy = 4;
        add(lblNumberType, gbc_lblNumberType);

        GridBagConstraints gbc_rdbtnNumberHex = new GridBagConstraints();
        gbc_rdbtnNumberHex.anchor = GridBagConstraints.WEST;
        gbc_rdbtnNumberHex.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNumberHex.gridx = 2;
        gbc_rdbtnNumberHex.gridy = 4;
        buttonGroup_2.add(rdbtnNumberHex);
        add(rdbtnNumberHex, gbc_rdbtnNumberHex);

        GridBagConstraints gbc_rdbtnNumberDec = new GridBagConstraints();
        gbc_rdbtnNumberDec.anchor = GridBagConstraints.WEST;
        gbc_rdbtnNumberDec.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNumberDec.gridx = 3;
        gbc_rdbtnNumberDec.gridy = 4;
        buttonGroup_2.add(rdbtnNumberDec);
        add(rdbtnNumberDec, gbc_rdbtnNumberDec);
    }

    @Override
    public void load(Options options) {
        rdbtnTypeSnes.setSelected(options.isPointerSnes());
        rdbtnTypePc.setSelected(!options.isPointerSnes());

        rdbtnPointerHex.setSelected(options.getPointerBase() == 16);
        rdbtnPointerDecimal.setSelected(options.getPointerBase() != 16);

        rdbtnNumberHex.setSelected(options.getNumberBase() == 16);
        rdbtnNumberDec.setSelected(options.getNumberBase() != 16);
    }

    @Override
    public void save(Options options) {
        options.setPointerSnes(rdbtnTypeSnes.isSelected());
        options.setPointerBase(rdbtnPointerHex.isSelected() ? 16 : 10);
        options.setNumberBase(rdbtnNumberHex.isSelected() ? 16 : 10);
    }
}
