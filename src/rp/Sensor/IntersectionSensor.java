package rp.Sensor;

import java.util.ArrayList;

import rp.Listener.IntersectionListener;
import rp.Listener.LineListener;

public class IntersectionSensor {
	private boolean leftDark, rightDark;
	private ArrayList<IntersectionListener> listeners;

	public IntersectionSensor(BlackLineSensor left, BlackLineSensor right) {
		this.listeners = new ArrayList<IntersectionListener>();
		left.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				leftDark = onLine;
				stateChanged();
			}
		});
		right.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				rightDark = onLine;
				stateChanged();
			}
		});
	}

	public IntersectionSensor addArriveListener(IntersectionListener listener) {
		this.listeners.add(listener);
		return this;
	}

	private void stateChanged() {
		if (this.leftDark && this.rightDark)
			for (IntersectionListener ls : this.listeners)
				ls.onIntersectionArrive();
	}
}