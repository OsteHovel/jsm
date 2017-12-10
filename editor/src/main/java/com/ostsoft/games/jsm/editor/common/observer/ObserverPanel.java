package com.ostsoft.games.jsm.editor.common.observer;

import com.ostsoft.games.jsm.editor.EditorData;
import com.ostsoft.games.jsm.editor.common.PanelData;
import com.ostsoft.games.jsm.editor.common.panel.EditorPanel;
import com.ostsoft.games.jsm.editor.common.panel.PanelEvent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ObserverPanel extends EditorPanel implements Observer {
    private final JList<String> observerJList;
    private final JList<String> eventJList;

    public ObserverPanel(EditorData editorData) {
        super(editorData, new PanelData(editorData));

        setLayout(new BorderLayout(0, 0));
        DefaultListModel<String> observerModel = new DefaultListModel<>();
        observerModel.addElement("Observer");
        observerModel.addElement("---------------------------------------------------");
        observerJList = new JList<>(observerModel);

        DefaultListModel<String> eventModel = new DefaultListModel<>();
        eventModel.addElement("Event log");
        eventModel.addElement("---------------------------------------------------");
        eventJList = new JList<>(eventModel);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(observerJList), new JScrollPane(eventJList));
        splitPane.setResizeWeight(0.5d);
        add(splitPane, BorderLayout.CENTER);
        editorData.addObserver(this);
        updateObservers();
    }

    private void updateObservers() {
        observerJList.removeAll();
        DefaultListModel<String> model1 = new DefaultListModel<>();
        List<Observer> observers = editorData.getObservers();
        for (Observer observer : observers) {
            model1.addElement(observer.toString());
        }
        observerJList.setModel(model1);
    }

    @Override
    public void handlePanelEvent(PanelEvent panelEvent) {
        if (panelEvent == PanelEvent.CLOSEPANEL) {
            editorData.removeObserver(this);
        }
    }

    @Override
    public void handleEvent(EventType eventType, String message) {
        updateObservers();
        ListModel<String> model = eventJList.getModel();
        if (model instanceof DefaultListModel) {
            ((DefaultListModel<String>) model).addElement(eventType.name() + " - " + message);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        updateObservers();
    }
}
