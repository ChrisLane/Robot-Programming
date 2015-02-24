package rp.Sensor;

import java.util.ArrayList;

import rp.Listener.IntersectionListener;
import rp.Listener.LineListener;

public class IntersectionSensor {
	private boolean leftDark, rightDark;
	private final ArrayList<IntersectionListener> listeners;
	private boolean onIntersection;

	public IntersectionSensor(BlackLineSensor left, BlackLineSensor right) {
		listeners = new ArrayList<>();

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

	public IntersectionSensor addChangeListener(IntersectionListener listener) {
		listeners.add(listener);
		return this;
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