package com.ostsoft.games.jsm.editor.common.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Observable {

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected List<Observer> getObservers() {
        return observers;
    }

    public void fireEvent(EventType eventType) {
        fireEvent(eventType, null);
    }

    public void fireEvent(EventType eventType, String message) {
        // To avoid concurrent modification exception when the event results in an observer being added
        List<Observer> notifyList = observers.stream().collect(Collectors.toList());
        for (Observer observer : notifyList) {
            observer.handleEvent(eventType, message);
        }
    }

}
