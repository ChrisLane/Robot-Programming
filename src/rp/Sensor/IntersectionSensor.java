package rp.Sensor;

import rp.Listener.IntersectionListener;
import rp.Listener.LineListener;

import java.util.ArrayList;

public class IntersectionSensor implements LineListener {
	private final ArrayList<IntersectionListener> listeners;
	private BlackLineSensor leftSensor;
	private boolean leftDark, rightDark, onIntersection;

	public IntersectionSensor(BlackLineSensor left, BlackLineSensor right, boolean onIntersection) {
		listeners = new ArrayList<>();
		leftSensor = left;

		this.onIntersection = onIntersection;
		this.leftDark = onIntersection;
		this.rightDark = onIntersection;

		left.addChangeListener(this);
		right.addChangeListener(this);
	}

	public IntersectionSensor addChangeListener(IntersectionListener listener) {
		listeners.add(listener);
		return this;
	}

	public boolean isOnIntersection() {
		return onIntersection;
	}

	public void lineChanged(BlackLineSensor sensor, boolean onLine, int lightValue) {
		if (sensor == leftSensor)
			leftDark = onLine;
		else
			rightDark = onLine;
		stateChanged();
	}

	private void stateChanged() {
		if (this.leftDark && this.rightDark && !onIntersection) {
			onIntersection = true;
			for (IntersectionListener ls : listeners)
				ls.onIntersectionArrive();
		}
		else if (!this.leftDark && !this.rightDark && onIntersection) {
			onIntersection = false;
			for (IntersectionListener ls : listeners)
				ls.onIntersectionDepart();
		}
	}

}