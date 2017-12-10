package com.ostsoft.games.jsm.editor.tilemap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class VerticalWrapList<T> extends JList<T> {
    private int cols;

    public VerticalWrapList(int cols) {
        super();
        this.cols = cols;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fixRowCountForVisibleColumns();
            }
        });
    }

    public VerticalWrapList() {
        super();
        this.cols = 0;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                fixRowCountForVisibleColumns();
            }
        });
    }

    public void fixRowCountForVisibleColumns() {
        if (cols > 0) {
            fixRowCountForColumns(this, cols);
        }
        else {
            fixRowCountForVisibleColumns(this);
        }
    }

    private void fixRowCountForVisibleColumns(JList<T> list) {
        fixRowCountForColumns(list, computeVisibleColumnCount(list));
    }

    private void fixRowCountForColumns(JList<T> list, int nCols) {
        int nItems = list.getModel().getSize();
        if (nCols <= 0) {
            nCols = 1;
        }

        // Compute the number of rows that will result in the desired number of
        // columns
        int nRows = nItems / nCols;
        if (nItems % nCols > 0) {
            nRows++;
        }
        list.setVisibleRowCount(nRows);
    }

    private int computeVisibleColumnCount(JList<T> list) {
        // It's assumed here that all cells have the same width. This method
        // could be modified if this assumption is false. If there was cell
        // padding, it would have to be accounted for here as well.
        Rectangle cellBounds = list.getCellBounds(0, 0);
        if (cellBounds == null) {
            return 0;
        }

        int cellWidth = cellBounds.width;
        Rectangle visibleRect = list.getVisibleRect();
        if (visibleRect == null) {
            return 0;
        }

        int width = visibleRect.width;
        return width / cellWidth;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
