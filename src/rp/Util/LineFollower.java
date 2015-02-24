package rp.Util;

import rp.Listener.IntersectionListener;
import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;
import rp.Sensor.IntersectionSensor;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class LineFollower implements IntersectionListener {
	private boolean onIntersection;

	public LineFollower(IntersectionSensor intersectionSensor, final DifferentialPilot pilot, BlackLineSensor lsLeft, BlackLineSensor lsRight, final int turnRate, final int turnAngle) {
		if (intersectionSensor != null) {
			intersectionSensor.addChangeListener(this);
			this.onIntersection = true;						// By default start on an intersection
		}

		lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Delay.msDelay(250);
						if (onLine && !onIntersection)
							pilot.steer(turnRate, -turnAngle);
						else
							pilot.forward();
					}
				}).start();
			}
		});

		lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Delay.msDelay(250);
						if (onLine && !onIntersection)
							pilot.steer(turnRate, turnAngle);
						else
							pilot.forward();
					}
				}).start();
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
