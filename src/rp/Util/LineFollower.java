package rp.Util;

import rp.Listener.LineListener;
import rp.Sensor.BlackLineSensor;

import lejos.robotics.navigation.DifferentialPilot;

public class LineFollower {

	public LineFollower(final DifferentialPilot pilot, BlackLineSensor lsLeft, BlackLineSensor lsRight, final int turnRate, final int turnAngle, final boolean immediateReturn) {
		lsLeft.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				if (onLine)
					pilot.steer(turnRate, -turnAngle, immediateReturn);
				else
					pilot.forward();
			}
		});

		lsRight.addChangeListener(new LineListener() {
			@Override
			public void lineChanged(boolean onLine, int lightValue) {
				if (onLine)
					pilot.steer(turnRate, turnAngle, immediateReturn);
				else
					pilot.forward();
			}
		});
	}
}
