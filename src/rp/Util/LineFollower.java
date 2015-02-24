package rp.Util;

import rp.Listener.IntersectionListener;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Sensor.IntersectionSensor;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class LineFollower implements IntersectionListener {
	private boolean onIntersection;

	public LineFollower(IntersectionSensor intersectionSensor, final DifferentialPilot pilot, BlackLineSensor lsLeft, BlackLineSensor lsRight, final int turnRate, final int turnAngle, final boolean immediateReturn) {
		if (intersectionSensor != null)
			intersectionSensor.addChangeListener(this);
		else
			throw new IllegalArgumentException("intersectionSensor cannot be null");

		lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				Delay.msDelay(250);
				if (onLine && !onIntersection)
					pilot.steer(turnRate, -turnAngle, immediateReturn);
				else
					pilot.forward();
			}
		});

		lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				Delay.msDelay(250);
				if (onLine && !onIntersection)
					pilot.steer(turnRate, turnAngle, immediateReturn);
				else
					pilot.forward();
			}
		});
	}

	@Override
	public void onIntersectionArrive() {
		onIntersection = true;
	}

	@Override
	public void onIntersectionDepart() {
		onIntersection = false;
	}
}
