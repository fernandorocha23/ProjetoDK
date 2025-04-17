package pt.iscte.poo.observer;

import java.util.ArrayList;
import java.util.List;

public class Observed {
	private List<Observer> observers = new ArrayList<Observer>();
	
	synchronized public void registerObserver(Observer o) {
		observers.add(o);
	}

	synchronized public void unregisterObserver(Observer o) {
		observers.remove(o);
	}
	
	synchronized public void notifyObservers() {
		for (Observer o: observers) {
			o.update(this);
		}
	}

	
}
